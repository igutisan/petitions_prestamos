package co.com.pragma.r2dbc.petitionsRepos;

import co.com.pragma.r2dbc.entity.PetitionEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetitionReactiveRepository extends ReactiveCrudRepository<PetitionEntity, String>, ReactiveQueryByExampleExecutor<PetitionEntity> {

}
