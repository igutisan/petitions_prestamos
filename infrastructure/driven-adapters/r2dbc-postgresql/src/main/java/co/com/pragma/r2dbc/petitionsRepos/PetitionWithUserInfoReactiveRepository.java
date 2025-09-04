package co.com.pragma.r2dbc.petitionsRepos;

import co.com.pragma.r2dbc.dto.PetitionWithUserInfoData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;



@Repository
public interface PetitionWithUserInfoReactiveRepository
        extends ReactiveCrudRepository<PetitionWithUserInfoData, String>, ReactiveQueryByExampleExecutor<PetitionWithUserInfoData> {

    @Query("""
       SELECT
                      l.loan_amount,
                      l.term,
                      l.loan_type,
                      l.loan_status,
                      c.email as user_email,
                      c.names || ' ' || c.last_names as user_name,
                      c.salary as user_salary
               FROM loans l
               JOIN clients c ON l.user_id = c.id
               WHERE (:status IS NULL OR l.loan_status = :status)
                ORDER BY l.id 
                LIMIT :limit OFFSET :offset
    """)
    Flux<PetitionWithUserInfoData> findAllWithUserInfo(@Param("status") String status,
                                                       @Param("limit") long limit,
                                                       @Param("offset") long offset);
}


