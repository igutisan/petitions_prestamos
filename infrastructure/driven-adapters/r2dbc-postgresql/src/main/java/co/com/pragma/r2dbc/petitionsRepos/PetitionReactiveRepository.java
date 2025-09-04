package co.com.pragma.r2dbc.petitionsRepos;

import co.com.pragma.r2dbc.dto.PetitionWithUserInfoData;
import co.com.pragma.r2dbc.entity.PetitionEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PetitionReactiveRepository extends ReactiveCrudRepository<PetitionEntity, String>, ReactiveQueryByExampleExecutor<PetitionEntity> {
    Flux<PetitionEntity> findByUserId(String userId);

    @Query("""
        SELECT p.id AS petition_id, p.loan_amount, p.term, p.loan_type, p.loan_status,
               u.email AS user_email, u.names || ' ' || u.last_names AS user_name, u.salary AS user_salary
        FROM petitions p
        JOIN users u ON p.user_id = u.id
    """)
    Flux<PetitionWithUserInfoData> findAllPetitionsWithUserInfo();

}
