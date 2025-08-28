package co.com.pragma.api;

import co.com.pragma.api.dto.CreatePetitionDTO;
import co.com.pragma.api.exceptions.ValidationException;
import co.com.pragma.api.mapper.PetitionDTOMapper;
import co.com.pragma.usecase.petition.PetitionUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final TransactionalOperator transactionalOperator;
    private final PetitionUseCase petitionUseCase;



    public Mono<ServerResponse> listenCreatePetition(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreatePetitionDTO.class)
                .flatMap(dto -> {
                    log.info("Validating dto");
                    Errors errors = new BeanPropertyBindingResult(dto, "dto");
                    validator.validate(dto,errors);

                    if (errors.hasErrors()){
                        log.error("Errors found in mapping dto");
                        Map<String, String> errorsMap = errors.getFieldErrors().stream()
                                .collect(java.util.stream.Collectors.toMap(
                                        FieldError::getField,
                                        DefaultMessageSourceResolvable::getDefaultMessage));
                        return Mono.<CreatePetitionDTO>error(new ValidationException(
                                "Error en la validación de los datos", errorsMap));
                    }
                    return Mono.just(dto);
                })
                .map(petitionMapper::toModel)
                .flatMap(petitionUseCase::createPetition)
                .as(transactionalOperator::transactional)
                .flatMap(savedPetition -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedPetition));

    }
}
