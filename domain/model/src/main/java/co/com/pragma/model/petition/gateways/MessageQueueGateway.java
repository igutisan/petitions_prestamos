package co.com.pragma.model.petition.gateways;

import reactor.core.publisher.Mono;

public interface MessageQueueGateway {
    Mono<String> sendMessageToNotificationQueue(Object message);
    Mono<String> sendMessageToAutomaticValidation(Object message);

}
