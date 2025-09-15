package co.com.pragma.model.petition.gateways;

import co.com.pragma.model.petition.Petition;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PetitionRepository {

    Mono<Petition> save(Petition petition);
    Mono<Petition>findById(String id);
    Flux<Petition> findAllActiveLoadsByUser(String userId);
}
