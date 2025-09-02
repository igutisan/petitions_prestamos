package co.com.pragma.r2dbc;

import co.com.pragma.model.client.Client;
import co.com.pragma.model.client.gateways.ClientRepository;

import co.com.pragma.r2dbc.entity.ClientEntity;

import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public class ClientRepositoryAdapter extends ReactiveAdapterOperations<
        Client,
        ClientEntity,
        String,
        ClientReactiveRepository
        > implements ClientRepository {
    public ClientRepositoryAdapter(ClientReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, Client.class));
    }


    @Override
    public Mono<Void> insertUser(Client client) {
        return repository.insertClient(client);
    }
}
