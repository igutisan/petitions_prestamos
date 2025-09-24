package co.com.pragma.api.openapi;

import co.com.pragma.api.dto.CreateClientDTO;
import co.com.pragma.api.dto.CreatePetitionDTO;
import co.com.pragma.api.dto.ErrorResponseDTO;
import co.com.pragma.api.dto.LoanTypeDTO;
import co.com.pragma.api.dto.PageResponse;
import co.com.pragma.api.dto.PetitionResponseDTO;
import co.com.pragma.api.dto.UpdatePetitionDTO;
import co.com.pragma.api.dto.UpdateResponseDTO;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.experimental.UtilityClass;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;
import static org.springdoc.core.fn.builders.securityrequirement.Builder.securityRequirementBuilder;

@UtilityClass
public class OpenApiDocumentation {

    private static final String TAG_PETITION = "Petitions";
    private static final String TAG_CLIENT = "Clients";
    private static final String TAG_LOAN_TYPE = "Loan Types";

    private static final String SUCCESS = "Success";
    private static final String CREATED = "Created";
    private static final String NO_CONTENT = "No Content";
    private static final String BAD_REQUEST = "Bad Request";
    private static final String NOT_FOUND = "Not Found";
    private static final String INTERNAL_ERROR = "Internal Server Error";

    private static final String SUCCESS_CODE = String.valueOf(HttpStatus.OK.value());
    private static final String CREATED_CODE = String.valueOf(HttpStatus.CREATED.value());
    private static final String NO_CONTENT_CODE = String.valueOf(HttpStatus.NO_CONTENT.value());
    private static final String BAD_REQUEST_CODE = String.valueOf(HttpStatus.BAD_REQUEST.value());
    private static final String NOT_FOUND_CODE = String.valueOf(HttpStatus.NOT_FOUND.value());
    private static final String INTERNAL_ERROR_CODE = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());

    public static Builder createPetition(Builder builder) {
        return builder
                .operationId("createPetition")
                .description("Create a new loan petition.")
                .tag(TAG_PETITION)
                .security(securityRequirementBuilder().name("bearerAuth"))
                .requestBody(requestBodyBuilder().required(true)
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(CreatePetitionDTO.class))))
                .response(responseBuilder().responseCode(CREATED_CODE).description(CREATED)
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(PetitionResponseDTO.class))))
                .response(responseBuilder().responseCode(BAD_REQUEST_CODE).description(BAD_REQUEST)
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponseDTO.class))))
                .response(responseBuilder().responseCode(INTERNAL_ERROR_CODE).description(INTERNAL_ERROR)
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponseDTO.class))));
    }

    public static Builder getAllPetitions(Builder builder) {
        return builder
                .operationId("getAllPetitions")
                .description("Get all petitions with user information, paginated.")
                .tag(TAG_PETITION)
                .security(securityRequirementBuilder().name("bearerAuth"))
                .parameter(parameterBuilder().name("page").in(ParameterIn.QUERY).description("Page number (default: 0)").required(false).schema(schemaBuilder().type("integer")))
                .parameter(parameterBuilder().name("size").in(ParameterIn.QUERY).description("Page size (default: 10)").required(false).schema(schemaBuilder().type("integer")))
                .response(responseBuilder().responseCode(SUCCESS_CODE).description(SUCCESS)
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(PageResponse.class))))
                .response(responseBuilder().responseCode(INTERNAL_ERROR_CODE).description(INTERNAL_ERROR)
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponseDTO.class))));
    }

    public static Builder updatePetitionStatus(Builder builder) {
        return builder
                .operationId("updatePetitionStatus")
                .description("Update the status of an existing petition by its ID.")
                .tag(TAG_PETITION)
                .security(securityRequirementBuilder().name("bearerAuth"))
                .parameter(parameterBuilder().name("id").in(ParameterIn.PATH).required(true).description("ID of the petition to update."))
                .requestBody(requestBodyBuilder().required(true)
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(UpdatePetitionDTO.class))))
                .response(responseBuilder().responseCode(SUCCESS_CODE).description(SUCCESS)
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(UpdateResponseDTO.class))))
                .response(responseBuilder().responseCode(BAD_REQUEST_CODE).description(BAD_REQUEST)
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponseDTO.class))))
                .response(responseBuilder().responseCode(NOT_FOUND_CODE).description(NOT_FOUND)
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponseDTO.class))))
                .response(responseBuilder().responseCode(INTERNAL_ERROR_CODE).description(INTERNAL_ERROR)
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponseDTO.class))));
    }

    public static Builder createClient(Builder builder) {
        return builder
                .operationId("createClient")
                .description("Create a new client.")
                .tag(TAG_CLIENT)
                .requestBody(requestBodyBuilder().required(true)
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(CreateClientDTO.class))))
                .response(responseBuilder().responseCode(CREATED_CODE).description(CREATED)
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(CreateClientDTO.class))))
                .response(responseBuilder().responseCode(BAD_REQUEST_CODE).description(BAD_REQUEST)
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponseDTO.class))))
                .response(responseBuilder().responseCode(INTERNAL_ERROR_CODE).description(INTERNAL_ERROR)
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponseDTO.class))));
    }

    public static Builder createLoanType(Builder builder) {
        return builder
                .operationId("createLoanType")
                .description("Create a new type of loan.")
                .tag(TAG_LOAN_TYPE)
                .security(securityRequirementBuilder().name("bearerAuth"))
                .requestBody(requestBodyBuilder().required(true)
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(LoanTypeDTO.class))))
                .response(responseBuilder().responseCode(CREATED_CODE).description(CREATED)
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(LoanTypeDTO.class))))
                .response(responseBuilder().responseCode(BAD_REQUEST_CODE).description(BAD_REQUEST)
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponseDTO.class))))
                .response(responseBuilder().responseCode(INTERNAL_ERROR_CODE).description(INTERNAL_ERROR)
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponseDTO.class))));
    }
}
