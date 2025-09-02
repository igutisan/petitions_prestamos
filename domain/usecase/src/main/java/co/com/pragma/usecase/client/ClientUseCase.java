package co.com.pragma.usecase.client;

import co.com.pragma.model.client.Client;

import co.com.pragma.model.client.gateways.ClientRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ClientUseCase {

    private final ClientRepository clientRepository;

    public Mono<Void> createClient(Client client) {
        System.out.println("Sincronizando cliente");
        return  clientRepository.insertUser(client);
    }
}
