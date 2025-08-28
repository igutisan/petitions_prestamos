package co.com.pragma.model.petition.gateways;

import co.com.pragma.model.petition.Petition;
import reactor.core.publisher.Mono;

public interface PetitionRepository {

    Mono<Petition> save(Petition petition);


}
