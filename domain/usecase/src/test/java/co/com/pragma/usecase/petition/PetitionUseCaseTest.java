
package co.com.pragma.usecase.petition;

import co.com.pragma.model.client.gateways.ClientRepository;
import co.com.pragma.model.petition.LoanStatus;
import co.com.pragma.model.petition.Loantypes;
import co.com.pragma.model.petition.Petition;
import co.com.pragma.model.petition.gateways.AuthenticationGateway;
import co.com.pragma.model.petition.gateways.LoggerGateway;
import co.com.pragma.model.petition.gateways.PetitionRepository;
import co.com.pragma.model.petitionwithuserinfo.PetitionWithUserInfo;
import co.com.pragma.model.petitionwithuserinfo.gateways.PetitionWithUserInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetitionUseCaseTest {

    @Mock
    private AuthenticationGateway clientGateway;
    @Mock
    private PetitionRepository petitionRepository;
    @Mock
    private PetitionWithUserInfoRepository petitionWithUserInfoRepository;
    @Mock
    private LoggerGateway loggerGateway;
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private PetitionUseCase petitionUseCase;

    @Test
    void createPetitionSuccessfully() {
        // Arrange
        Petition petition = Petition.builder()
                .id(UUID.randomUUID())
                .userId("user123")
                .loanAmount(new BigDecimal("10000"))
                .term("12")
                .loanType(Loantypes.PERSONAL)
                .loanStatus(LoanStatus.PENDING_REVIEW)
                .build();
        when(petitionRepository.save(any(Petition.class))).thenReturn(Mono.just(petition));

        // Act
        Mono<Petition> result = petitionUseCase.createPetition(petition);

        // Assert
        StepVerifier.create(result)
                .expectNext(petition)
                .verifyComplete();
    }

    @Test
    void getAllPetitionsWithUserInfoSuccessfully() {
        // Arrange
        PetitionWithUserInfo p = PetitionWithUserInfo.builder().userName("test").build();
        when(petitionWithUserInfoRepository.findAllWithUserInfo(anyString(), anyInt(), anyInt()))
                .thenReturn(Flux.just(p));

        // Act
        Flux<PetitionWithUserInfo> result = petitionUseCase.getAllPetitionsWithUserInfo("PENDING", 0, 10);

        // Assert
        StepVerifier.create(result)
                .expectNext(p)
                .verifyComplete();
    }

    @Test
    void getAllPetitionsWithUserInfoEmpty() {
        // Arrange
        when(petitionWithUserInfoRepository.findAllWithUserInfo(anyString(), anyInt(), anyInt()))
                .thenReturn(Flux.empty());

        // Act
        Flux<PetitionWithUserInfo> result = petitionUseCase.getAllPetitionsWithUserInfo("APPROVED", 1, 5);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void getAllPetitionsWithUserInfoError() {
        // Arrange
        when(petitionWithUserInfoRepository.findAllWithUserInfo(anyString(), anyInt(), anyInt()))
                .thenReturn(Flux.error(new RuntimeException("Database Error")));

        // Act
        Flux<PetitionWithUserInfo> result = petitionUseCase.getAllPetitionsWithUserInfo("REJECTED", 0, 20);

        // Assert
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}
