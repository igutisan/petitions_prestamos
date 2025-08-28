package co.com.pragma.consumer;

import co.com.pragma.model.petition.gateways.AuthenticationGateway;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements AuthenticationGateway/* implements Gateway from domain */{
    private final WebClient client;



    @Override
    public Mono<Boolean> validateUser(String dni) {
        return client.get()
                .uri("/{dni}", dni)
                .retrieve()
                .bodyToMono(ExistsResponse.class)
                .map(ExistsResponse::exists)
                .onErrorReturn(false);
    }
}
