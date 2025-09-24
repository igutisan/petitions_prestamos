package co.com.pragma.usecase.petition;

import co.com.pragma.model.client.Client;
import co.com.pragma.model.client.gateways.ClientRepository;
import co.com.pragma.model.exceptions.NotFoundException;
import co.com.pragma.model.loantype.LoanType;
import co.com.pragma.model.loantype.gateways.LoanTypeRepository;
import co.com.pragma.model.petition.LoanStatus;
import co.com.pragma.model.petition.Petition;
import co.com.pragma.model.petition.gateways.MessageQueueGateway;
import co.com.pragma.model.petition.gateways.PetitionRepository;
import co.com.pragma.model.petitionwithuserinfo.PetitionWithUserInfo;
import co.com.pragma.model.petitionwithuserinfo.gateways.PetitionWithUserInfoRepository;
import co.com.pragma.usecase.petition.dto.ValidationResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyInt;


@ExtendWith(MockitoExtension.class)
class PetitionUseCaseTest {

    @Mock
    private PetitionRepository petitionRepository;
    @Mock
    private PetitionWithUserInfoRepository petitionWithUserInfoRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private LoanTypeRepository loanTypeRepository;
    @Mock
    private MessageQueueGateway messageQueueGateway;

    @InjectMocks
    private PetitionUseCase petitionUseCase;

    @Test
    void createPetitionSuccessfullyWithAutomaticValidation() {
        // Arrange
        UUID loanTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Petition petition = Petition.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .loanAmount(new BigDecimal("10000"))
                .term(12)
                .loanTypeId(loanTypeId)
                .loanStatus(LoanStatus.PENDING_REVIEW)
                .build();

        LoanType loanType = LoanType.builder().id(loanTypeId).isAutomaticValidation(true).build();
        Client client = Client.builder().id(userId).salary(new BigDecimal("50000")).email("test@test.com").names("test").lastNames("test").build();

        when(loanTypeRepository.findById(anyString())).thenReturn(Mono.just(loanType));
        when(clientRepository.findById(anyString())).thenReturn(Mono.just(client));
        when(petitionRepository.save(any(Petition.class))).thenReturn(Mono.just(petition));
        when(petitionWithUserInfoRepository.findAllActiveLoadsWithUserInfo(any(UUID.class))).thenReturn(Flux.empty());
        when(messageQueueGateway.sendMessageToAutomaticValidation(any())).thenReturn(Mono.empty());

        // Act
        Mono<Petition> result = petitionUseCase.createPetition(petition);

        // Assert
        StepVerifier.create(result)
                .expectNext(petition)
                .verifyComplete();

        // Verify that automatic validation message was sent
        verify(messageQueueGateway).sendMessageToAutomaticValidation(any());
    }

    @Test
    void createPetitionSuccessfullyWithoutAutomaticValidation() {
        // Arrange
        UUID loanTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Petition petition = Petition.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .loanAmount(new BigDecimal("10000"))
                .term(12)
                .loanTypeId(loanTypeId)
                .loanStatus(LoanStatus.PENDING_REVIEW)
                .build();

        LoanType loanType = LoanType.builder().id(loanTypeId).isAutomaticValidation(false).build();
        Client client = Client.builder().id(userId).build();

        when(loanTypeRepository.findById(anyString())).thenReturn(Mono.just(loanType));
        when(clientRepository.findById(anyString())).thenReturn(Mono.just(client));
        when(petitionRepository.save(any(Petition.class))).thenReturn(Mono.just(petition));

        // Act
        Mono<Petition> result = petitionUseCase.createPetition(petition);

        // Assert
        StepVerifier.create(result)
                .expectNext(petition)
                .verifyComplete();
    }

    @Test
    void createPetitionLoanTypeNotFound() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Petition petition = Petition.builder().loanTypeId(UUID.randomUUID()).userId(userId).build();
        Client client = Client.builder().id(userId).build();

        when(loanTypeRepository.findById(anyString())).thenReturn(Mono.empty());
        when(clientRepository.findById(anyString())).thenReturn(Mono.just(client));
        when(petitionRepository.save(any(Petition.class))).thenReturn(Mono.just(petition));

        // Act
        Mono<Petition> result = petitionUseCase.createPetition(petition);

        // Assert
        StepVerifier.create(result)
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void createPetitionClientNotFound() {
        // Arrange
        UUID loanTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Petition petition = Petition.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .loanTypeId(loanTypeId)
                .build();
        LoanType loanType = LoanType.builder().id(loanTypeId).isAutomaticValidation(true).build();

        when(loanTypeRepository.findById(anyString())).thenReturn(Mono.just(loanType));
        when(clientRepository.findById(anyString())).thenReturn(Mono.empty());
        when(petitionRepository.save(any(Petition.class))).thenReturn(Mono.just(petition));
        when(petitionWithUserInfoRepository.findAllActiveLoadsWithUserInfo(any(UUID.class))).thenReturn(Flux.empty());


        // Act
        Mono<Petition> result = petitionUseCase.createPetition(petition);

        // Assert
        StepVerifier.create(result)
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void updatePetitionStatusSuccessfully() {
        // Arrange
        UUID petitionId = UUID.randomUUID();
        ValidationResponseDTO validationResponseDTO = new ValidationResponseDTO(petitionId.toString(), LoanStatus.APPROVED);
        Petition petition = Petition.builder().id(petitionId).loanStatus(LoanStatus.PENDING_REVIEW).build();
        PetitionWithUserInfo petitionWithUserInfo = PetitionWithUserInfo.builder()
                .id(petitionId.toString())
                .loanStatus(LoanStatus.APPROVED)
                .userName("Test User")
                .userEmail("test@test.com")
                .term(12)
                .loanAmount(new BigDecimal("10000"))
                .interestRate(10.0)
                .build();
        ArgumentCaptor<Petition> petitionCaptor = ArgumentCaptor.forClass(Petition.class);

        when(petitionRepository.findById(anyString())).thenReturn(Mono.just(petition));
        when(petitionRepository.save(petitionCaptor.capture())).thenReturn(Mono.just(petition));
        when(petitionWithUserInfoRepository.findByIdWithUserInfo(any(UUID.class))).thenReturn(Mono.just(petitionWithUserInfo));
        when(messageQueueGateway.sendMessageToNotificationQueue(any())).thenReturn(Mono.empty());
        when(messageQueueGateway.sendMessageToAcceptedPetitionsQueue(any())).thenReturn(Mono.empty());

        // Act
        Mono<PetitionWithUserInfo> result = petitionUseCase.updatePetitionStatus(validationResponseDTO);

        // Assert
        StepVerifier.create(result)
                .expectNext(petitionWithUserInfo)
                .verifyComplete();

        assertEquals(LoanStatus.APPROVED, petitionCaptor.getValue().getLoanStatus());
        // Verify that messages were sent to the queues
        verify(messageQueueGateway).sendMessageToNotificationQueue(any());
        verify(messageQueueGateway).sendMessageToAcceptedPetitionsQueue(any());
    }

    @Test
    void updatePetitionStatusPetitionNotFound() {
        // Arrange
        ValidationResponseDTO validationResponseDTO = new ValidationResponseDTO(UUID.randomUUID().toString(), LoanStatus.APPROVED);
        when(petitionRepository.findById(anyString())).thenReturn(Mono.empty());

        // Act
        Mono<PetitionWithUserInfo> result = petitionUseCase.updatePetitionStatus(validationResponseDTO);

        // Assert
        StepVerifier.create(result)
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void getAllPetitionsWithUserInfoSuccessfully() {
        // Arrange
        PetitionWithUserInfo p = PetitionWithUserInfo.builder()
                .userName("test")
                .interestRate(10.0)
                .loanAmount(new BigDecimal("10000"))
                .term(12)
                .build();
        when(petitionWithUserInfoRepository.findAllWithUserInfo(anyString(), anyInt(), anyInt()))
                .thenReturn(Flux.just(p));

        // Act
        Flux<PetitionWithUserInfo> result = petitionUseCase.getAllPetitionsWithUserInfo("PENDING", 0, 10);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(petition -> {
                    assertEquals(877.16, petition.getMonthlyAmountRequest().doubleValue(), 0.01);
                    return true;
                })
                .verifyComplete();
    }
    
    @Test
    void getAllPetitionsWithUserInfoSuccessfullyMonthlyRateZero() {
        // Arrange
        PetitionWithUserInfo p = PetitionWithUserInfo.builder()
                .userName("test")
                .interestRate(0.0)
                .loanAmount(new BigDecimal("12000"))
                .term(12)
                .build();
        when(petitionWithUserInfoRepository.findAllWithUserInfo(anyString(), anyInt(), anyInt()))
                .thenReturn(Flux.just(p));

        // Act
        Flux<PetitionWithUserInfo> result = petitionUseCase.getAllPetitionsWithUserInfo("PENDING", 0, 10);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(petition -> {
                    assertEquals(1000.0, petition.getMonthlyAmountRequest().doubleValue());
                    return true;
                })
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

    @Test
    void countByStatusSuccessfully() {
        // Arrange
        when(petitionWithUserInfoRepository.countByStatus(anyString())).thenReturn(Mono.just(5L));

        // Act
        Mono<Long> result = petitionUseCase.countByStatus("PENDING");

        // Assert
        StepVerifier.create(result)
                .expectNext(5L)
                .verifyComplete();
    }

    @Test
    void countByStatusError() {
        // Arrange
        when(petitionWithUserInfoRepository.countByStatus(anyString())).thenReturn(Mono.error(new RuntimeException("Database Error")));

        // Act
        Mono<Long> result = petitionUseCase.countByStatus("PENDING");

        // Assert
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}