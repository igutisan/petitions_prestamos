package co.com.pragma.api;

import co.com.pragma.api.dto.*;
import co.com.pragma.api.exceptions.ValidationException;
import co.com.pragma.api.mapper.ClientDTOMapper;
import co.com.pragma.api.mapper.LoanTypeMapper;
import co.com.pragma.api.mapper.PetitionDTOMapper;
import co.com.pragma.api.mapper.PetitionWithUserInfoDTOMapper;
import co.com.pragma.model.petition.LoanStatus;
import co.com.pragma.model.petitionwithuserinfo.PetitionWithUserInfo;
import co.com.pragma.usecase.client.ClientUseCase;
import co.com.pragma.usecase.loantype.LoanTypeUseCase;
import co.com.pragma.usecase.petition.PetitionUseCase;
import co.com.pragma.usecase.petition.dto.ValidationResponseDTO;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class Handler {

    private final Validator validator;
    private final PetitionDTOMapper petitionMapper;
    private final ClientDTOMapper clientMapper;
    private final LoanTypeMapper loanTypeMapper;
    private final PetitionWithUserInfoDTOMapper petitionWithUserInfoDTOMapper;
    private final TransactionalOperator transactionalOperator;
    private final PetitionUseCase petitionUseCase;
    private final ClientUseCase clientUseCase;
    private final LoanTypeUseCase loanTypeUseCase;


    public Mono<ServerResponse> listenCreateUser(ServerRequest serverRequest) {
        log.info("Received request to create a new client");
        return serverRequest.bodyToMono(CreateClientDTO.class)
                .map(clientMapper::toModel)
                .flatMap(clientUseCase::createClient)
                .doOnSuccess(ignored -> log.info("Successfully created client"))
                .doOnError(e -> log.error("Error creating client", e))
                .then(
                        ServerResponse.status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON).build()
                );
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
                    petition.setUserId(UUID.fromString(tuple.getT2().getName()));
                    petition.setLoanStatus(LoanStatus.PENDING_REVIEW);
                    log.info("Mapped petition");
                    return petition;
                })
                .flatMap(petitionUseCase::createPetition)
                .doOnSuccess(p -> log.info("Successfully created petition "))
                .doOnError(e -> log.error("Error creating petition", e))
                .as(transactionalOperator::transactional)
                .then(
                        ServerResponse.status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON).build()
                );
    }
    @PreAuthorize("hasRole('ASESOR')")
    public Mono<ServerResponse> listenUpdatePetitionStatus(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        log.info("Received request to update status for petition id={}", id);

        return serverRequest.bodyToMono(UpdatePetitionDTO.class)
                .flatMap(dto -> {
                    log.debug("Validating petition DTO: {}", dto);

                    Errors errors = new BeanPropertyBindingResult(dto, "dto");
                    validator.validate(dto, errors);

                    if (errors.hasErrors()) {
                        Map<String, String> errorsMap = errors.getFieldErrors().stream()
                                .collect(Collectors.toMap(
                                        FieldError::getField,
                                        DefaultMessageSourceResolvable::getDefaultMessage));

                        log.warn("Validation failed for petition DTO: {}", errorsMap);
                        return Mono.error(new ValidationException(
                                "Error en la validación de los datos", errorsMap));
                    }

                    log.debug("Petition DTO validation successful, proceeding with update");
                    return petitionUseCase.updatePetitionStatus(new ValidationResponseDTO().builder()
                            .petitionId(id)
                            .status(dto.status())
                            .build());

                })
                .doOnSuccess(p -> log.info("Successfully updated petition with id={}", id))
                .doOnError(e -> log.error("Error updating petition", e))
                .as(transactionalOperator::transactional)
                .map(petitionWithUserInfoDTOMapper::toDTO)
                .flatMap(updatedPetition -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(updatedPetition));
    }

    @PreAuthorize("hasRole('ASESOR')")
    public Mono<ServerResponse> getAllPetitionsWithUserInfo(ServerRequest request) {
        String status = request.queryParam("status").orElse(null);
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));

        Mono<Long> totalMono = petitionUseCase.countByStatus(status);
        Flux<PetitionWithUserInfo> data = petitionUseCase.getAllPetitionsWithUserInfo(status, page, size);
        Mono<PageResponse<PetitionWithUserInfo>> response = totalMono.flatMap(total ->
                data.collectList().map(content -> PageResponse.<PetitionWithUserInfo>builder()
                        .content(content)
                        .page(page)
                        .size(size)
                        .totalElements(total)
                        .totalPages((int) Math.ceil((double) total / size))
                        .build()
                )
        );
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response, PetitionWithUserInfo.class)
                .doOnError(e -> log.error("Error fetching petitions", e));
    }



    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ServerResponse> listenCreateLoanType(ServerRequest serverRequest) {
        log.info("Received request to create a new loan type");
        return serverRequest.bodyToMono(LoanTypeDTO.class)
                .map(loanTypeMapper::toModel)
                .flatMap(loanTypeUseCase::save)
                .doOnSuccess(loanType -> log.info("Successfully created loan type"))
                .doOnError(e -> log.error("Error creating loan type", e))
                .map(loanTypeMapper::toDto)
                .as(transactionalOperator::transactional)
                .flatMap(savedLoanType -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedLoanType));
    }

}


//Hacer la HU5
//Hacer la HU6