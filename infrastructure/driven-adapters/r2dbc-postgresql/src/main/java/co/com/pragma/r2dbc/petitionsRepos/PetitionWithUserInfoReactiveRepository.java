package co.com.pragma.r2dbc.petitionsRepos;

import co.com.pragma.model.petitionwithuserinfo.PetitionWithUserInfo;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


@Repository
public interface PetitionWithUserInfoReactiveRepository
        extends ReactiveCrudRepository<PetitionWithUserInfo, String>, ReactiveQueryByExampleExecutor<PetitionWithUserInfo> {

    @Query("""
       SELECT 
              l.id, 
              l.loan_amount, 
              l.term, 
              l.loan_status,
              lt.name AS loan_type_name,
              lt.interest_rate AS interest_rate,
              c.email   AS user_email,
              c.names || ' ' || c.last_names AS user_name,
              c.salary  AS user_salary
       FROM loans l
       JOIN clients c ON l.user_id = c.id
       JOIN loan_types lt ON l.loan_type_id = lt.id
       WHERE (:status IS NULL OR l.loan_status = :status)
       ORDER BY l.id
       LIMIT :limit OFFSET :offset
    """)
    Flux<PetitionWithUserInfo> findAllWithUserInfo(@Param("status") String status,
                                                   @Param("limit") long limit,
                                                   @Param("offset") long offset);


    @Query("""
       SELECT 
              l.id, 
              l.loan_amount, 
              l.term, 
              l.loan_status,
              lt.name AS loan_type_name,
              lt.interest_rate AS interest_rate,
              c.email   AS user_email,
              c.names || ' ' || c.last_names AS user_name,
              c.salary  AS user_salary
       FROM loans l
       JOIN clients c ON l.user_id = c.id
       JOIN loan_types lt ON l.loan_type_id = lt.id
       WHERE (l.id = :id)
    """)
    Mono<PetitionWithUserInfo> findByIdWithUserInfo(UUID id);

    @Query("""
       SELECT
           l.id,
           l.loan_amount,
           l.term,
           l.loan_status,
           lt.name AS loan_type_name,
           lt.interest_rate AS interest_rate,
           c.email   AS user_email,
           c.names || ' ' || c.last_names AS user_name,
           c.salary  AS user_salary
       FROM loans l
       JOIN clients c ON l.user_id = c.id
       JOIN loan_types lt ON l.loan_type_id = lt.id
       WHERE l.user_id = :userId
         AND l.loan_status = 'APPROVED'
       ORDER BY l.id
    """)
    Flux<PetitionWithUserInfo> findAllActiveLoadsWithUserInfo(UUID id);


    @Query("SELECT COUNT(*) FROM loans l WHERE (:status IS NULL OR l.loan_status = :status)")
    Mono<Long> countByStatus(@Param("status") String status);
}


