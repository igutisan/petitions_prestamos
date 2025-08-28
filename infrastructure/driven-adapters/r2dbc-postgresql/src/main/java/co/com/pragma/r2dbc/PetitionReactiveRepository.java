package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.entity.PetitionEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

// TODO: This file is just an example, you should delete or modify it
public interface PetitionReactiveRepository extends ReactiveCrudRepository<PetitionEntity, String>, ReactiveQueryByExampleExecutor<PetitionEntity> {

}
