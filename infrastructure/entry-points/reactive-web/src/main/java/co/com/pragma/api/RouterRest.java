package co.com.pragma.api;

import co.com.pragma.api.openapi.OpenApiDocumentation;
import org.springdoc.webflux.core.fn.SpringdocRouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handlerV1) {
        return SpringdocRouteBuilder.route()
                .POST("/api/v1/petition", handlerV1::listenCreatePetition, OpenApiDocumentation::createPetition)
                .GET("/api/v1/petition", handlerV1::getAllPetitionsWithUserInfo, OpenApiDocumentation::getAllPetitions)
                .POST("/api/v1/users", handlerV1::listenCreateUser, OpenApiDocumentation::createClient)
                .POST("/api/v1/loantype", handlerV1::listenCreateLoanType, OpenApiDocumentation::createLoanType)
                .PATCH("/api/v1/petition/{id}", handlerV1::listenUpdatePetitionStatus, OpenApiDocumentation::updatePetitionStatus)
                .build();
    }
}
