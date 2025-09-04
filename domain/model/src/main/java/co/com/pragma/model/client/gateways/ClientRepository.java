package co.com.pragma.model.client.gateways;

import co.com.pragma.model.client.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientRepository {
    Mono<Void> insertUser(Client client);

    Mono<Client> findById(String userId);
    Flux<Client> findAll();
}
