package co.com.pragma.sqs.sender;

import co.com.pragma.sqs.sender.config.SQSSenderProperties;
import co.com.pragma.usecase.petition.PetitionUseCase;
import co.com.pragma.usecase.petition.dto.ValidationResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class SQSListener {
    private final SqsAsyncClient sqsAsyncClient;
    private final SQSSenderProperties properties;
    private final PetitionUseCase petitionUseCase;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void startListening() {
        SQSSenderProperties.ListenerProperties listenerProps = properties.listener();
        log.info(" Iniciando listener para la cola SQS: {}", listenerProps.responseValidationQueueUrl());

        Flux.defer(() -> receiveMessages(listenerProps))
                .repeat()
                .flatMap(this::processAndDeleteMessage, listenerProps.concurrency())
                .onErrorContinue((throwable, o) -> log.error("Error procesando mensaje, se continuará con el siguiente.", throwable))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }


    private Flux<Message> receiveMessages(SQSSenderProperties.ListenerProperties listenerProps) {
        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl(listenerProps.responseValidationQueueUrl())
                .maxNumberOfMessages(listenerProps.maxMessages())
                .waitTimeSeconds(listenerProps.waitTime())
                .build();

        return Mono.fromFuture(sqsAsyncClient.receiveMessage(request))
                .doOnNext(response -> log.debug("{} mensajes recibidos.", response.messages().size()))
                .flatMapMany(response -> Flux.fromIterable(response.messages()));
    }


    private Mono<Void> processAndDeleteMessage(Message message) {
        return Mono.fromCallable(() -> {
                    log.info("Deserializando mensaje ID: {}", message.messageId());
                    return objectMapper.readValue(message.body(), ValidationResponseDTO.class);
                })
                .flatMap(petitionUseCase::updatePetitionStatus)
                .then(deleteMessage(message))
                .doOnSuccess(v -> log.info("Petición actualizada y mensaje {} borrado exitosamente.", message.messageId()));
    }


    private Mono<Void> deleteMessage(Message message) {
        DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                .queueUrl(properties.listener().responseValidationQueueUrl())
                .receiptHandle(message.receiptHandle())
                .build();
        return Mono.fromFuture(sqsAsyncClient.deleteMessage(deleteRequest)).then();
    }

}
