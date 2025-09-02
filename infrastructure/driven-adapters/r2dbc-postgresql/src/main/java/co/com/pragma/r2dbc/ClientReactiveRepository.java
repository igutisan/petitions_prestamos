package co.com.pragma.r2dbc;

import co.com.pragma.model.client.Client;
import co.com.pragma.r2dbc.entity.ClientEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ClientReactiveRepository extends ReactiveCrudRepository<ClientEntity, String>, ReactiveQueryByExampleExecutor<ClientEntity> {
    @Modifying
    @Query("INSERT INTO clients (id, names, last_names, dni, dob, phone, address, email, salary) " +
            "VALUES (:#{#client.id}, :#{#client.names}, :#{#client.lastNames}, :#{#client.dni}, " +
            ":#{#client.dob}, :#{#client.phone}, :#{#client.address}, :#{#client.email}, :#{#client.salary})")

    Mono<Void> insertClient(@Param("client") Client client);

}
