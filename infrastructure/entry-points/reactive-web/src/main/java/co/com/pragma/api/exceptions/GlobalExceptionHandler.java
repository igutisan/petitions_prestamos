package co.com.pragma.api.exceptions;



import co.com.pragma.model.petition.exceptions.InvalidUser;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(-2)
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {
    public GlobalExceptionHandler(ErrorAttributes errorAttributes,
                                  ApplicationContext applicationContext,
                                  ServerCodecConfigurer codecConfigurer) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        super.setMessageWriters(codecConfigurer.getWriters());
        super.setMessageReaders(codecConfigurer.getReaders());
    }


    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::formatErrorResponse);
    }

    private Mono<ServerResponse> formatErrorResponse(ServerRequest request) {
        Throwable error = getError(request);


        if (error instanceof ValidationException validationException) {
            return buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    "VALIDATION_ERROR",
                    validationException.getMessage(),
                    request.path(),
                    validationException.getFieldErrors()
            );
        }


        // Manejar InavlidUserException
        if (error instanceof InvalidUser) {
            return buildErrorResponse(
                    HttpStatus.NOT_FOUND,
                    "NOT_FOUND_USER",
                    error.getMessage(),
                    request.path()
            );
        }

        // Manejar TokenException
        if (error instanceof TokenException) {
            return buildErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    "INVALID_TOKEN",
                    error.getMessage(),
                    request.path()
            );
        }


        // Manejar IllegalArgumentException
        if (error instanceof IllegalArgumentException) {
            return buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    "INVALID_ARGUMENT",
                    error.getMessage(),
                    request.path()
            );
        }

        // Error genérico
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "Error interno del servidor",
                request.path()
        );
    }

    private Mono<ServerResponse> buildErrorResponse(HttpStatus status, String code, String message, String path) {
        return buildErrorResponse(status, code, message, path, null);
    }

    private Mono<ServerResponse> buildErrorResponse(HttpStatus status, String code, String message, String path,
                                                    Map<String, String> fieldErrors) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());
        errorResponse.put("code", code);
        errorResponse.put("message", message != null ? message : "Sin mensaje disponible");
        errorResponse.put("path", path);

        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            errorResponse.put("fieldErrors", fieldErrors);
        }

        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorResponse);
    }





}
