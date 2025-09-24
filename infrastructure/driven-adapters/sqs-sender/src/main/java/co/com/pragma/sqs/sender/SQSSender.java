package co.com.pragma.sqs.sender;

import co.com.pragma.model.petition.gateways.MessageQueueGateway;
import co.com.pragma.sqs.sender.config.SQSSenderProperties;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class SQSSender implements MessageQueueGateway {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;
    private final ObjectMapper objectMapper;


    @Override
    public Mono<Void> sendMessageToAcceptedPetitionsQueue(Object message) {
        return sendToQueue(message, properties.sender().acceptedPetitionsQueueUrl());
    }

    @Override
    public Mono<Void> sendMessageToNotificationQueue(Object message) {
        return sendToQueue(message, properties.sender().notificationQueueUrl());
    }

    @Override
    public Mono<Void> sendMessageToAutomaticValidation(Object message) {
        return sendToQueue(message, properties.sender().automaticValidationQueueUrl());
    }

    private Mono<Void> sendToQueue(Object message, String queueUrl) {
        return Mono.fromCallable(() -> {
                    String json = objectMapper.writeValueAsString(message);
                    return SendMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .messageBody(json)
                            .build();
                })
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent to {} with id {}", queueUrl, response.messageId()))
                .then();

    }


}
