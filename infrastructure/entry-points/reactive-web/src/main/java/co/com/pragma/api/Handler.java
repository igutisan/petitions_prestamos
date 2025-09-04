package co.com.pragma.api;

import co.com.pragma.api.dto.CreatePetitionDTO;
import co.com.pragma.api.dto.CreateClientDTO;
import co.com.pragma.api.exceptions.ValidationException;
import co.com.pragma.api.mapper.ClientDTOMapper;
import co.com.pragma.api.mapper.PetitionDTOMapper;
import co.com.pragma.model.petition.LoanStatus;
import co.com.pragma.model.petitionwithuserinfo.PetitionWithUserInfo;
import co.com.pragma.usecase.client.ClientUseCase;
import co.com.pragma.usecase.petition.PetitionUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class Handler {

    private final Validator validator;
    private final PetitionDTOMapper petitionMapper;
    private final ClientDTOMapper clientMapper;
    private final TransactionalOperator transactionalOperator;
    private final PetitionUseCase petitionUseCase;
    private final ClientUseCase clientUseCase;


    public Mono<ServerResponse> listenCreateUser(ServerRequest serverRequest) {
        log.info("Received request to create a new client");
        return serverRequest.bodyToMono(CreateClientDTO.class)
                .map(clientMapper::toModel)
                .flatMap(clientUseCase::createClient)
                .doOnSuccess(client -> log.info("Successfully created client"))
                .doOnError(e -> log.error("Error creating client", e))
                .as(transactionalOperator::transactional)
                .flatMap(savedClient -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedClient));
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public Mono<ServerResponse> listenCreatePetition(ServerRequest serverRequest) {
        log.info("Received request to create a new petition");
        return serverRequest.bodyToMono(CreatePetitionDTO.class)
                .flatMap(dto -> {
                    log.debug("Validating petition DTO");
                    Errors errors = new BeanPropertyBindingResult(dto, "dto");
                    validator.validate(dto, errors);

                    if (errors.hasErrors()) {
                        Map<String, String> errorsMap = errors.getFieldErrors().stream()
                                .collect(java.util.stream.Collectors.toMap(
                                        FieldError::getField,
                                        DefaultMessageSourceResolvable::getDefaultMessage));
                        log.warn("Validation failed for petition DTO: {}", errorsMap);
                        return Mono.<CreatePetitionDTO>error(new ValidationException(
                                "Error en la validación de los datos", errorsMap));
                    }

                    log.debug("Petition DTO validation successful");
                    return Mono.just(dto);
                })
                .zipWith(serverRequest.principal())
                .map(tuple -> {
                    var petition = petitionMapper.toModel(tuple.getT1());
                    petition.setUserId(tuple.getT2().getName());
                    petition.setLoanStatus(LoanStatus.PENDING_REVIEW);
                    log.info("Mapped petition");
                    return petition;
                })
                .flatMap(petitionUseCase::createPetition)
                .doOnSuccess(p -> log.info("Successfully created petition with id={}", p.getId()))
                .doOnError(e -> log.error("Error creating petition", e))
                .as(transactionalOperator::transactional)
                .flatMap(savedPetition -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedPetition));
    }

    @PreAuthorize("hasRole('ASESOR')")
    public Mono<ServerResponse> getAllPetitionsWithUserInfo(ServerRequest request) {
        String status = request.queryParam("status").orElse("PENDING_REVIEW");
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));

        log.info("Fetching petitions with status={} page={} size={}", status, page, size);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(petitionUseCase.getAllPetitionsWithUserInfo(status, page, size), PetitionWithUserInfo.class)
                .doOnSuccess(resp -> log.info("Successfully fetched petitions for status={} page={} size={}", status, page, size))
                .doOnError(e -> log.error("Error fetching petitions", e));
    }
}
