package co.com.pragma.model.petition.gateways;

import reactor.core.publisher.Mono;

public interface MessageQueueGateway {
    Mono<Void> sendMessageToNotificationQueue(Object message);
    Mono<Void> sendMessageToAutomaticValidation(Object message);
    Mono<Void> sendMessageToAcceptedPetitionsQueue(Object message);

}
