package co.com.pragma.r2dbc;

import co.com.pragma.model.petition.Petition;

import co.com.pragma.model.petition.gateways.PetitionRepository;
import co.com.pragma.r2dbc.entity.PetitionEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class PetitionRepositoryAdapter extends ReactiveAdapterOperations<
        Petition,
        PetitionEntity,
        String,
        PetitionReactiveRepository
> implements PetitionRepository {
    public PetitionRepositoryAdapter(PetitionReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, Petition.class));
    }

    @Override
    public Mono<Petition> save(Petition petition){
        return super.save(petition);
    }

}
