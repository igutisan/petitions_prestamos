package co.com.pragma.model.petition.gateways;

import reactor.core.publisher.Mono;

public interface AuthenticationGateway {
    Mono<Boolean> validateUser(String id);
}
