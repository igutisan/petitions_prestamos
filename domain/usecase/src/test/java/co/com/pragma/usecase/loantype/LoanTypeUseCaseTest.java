package co.com.pragma.usecase.loantype;

import co.com.pragma.model.loantype.LoanType;
import co.com.pragma.model.loantype.gateways.LoanTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanTypeUseCaseTest {

    @Mock
    private LoanTypeRepository loanTypeRepository;

    @InjectMocks
    private LoanTypeUseCase loanTypeUseCase;

    @Test
    void saveLoanTypeSuccessfully() {
        // Arrange
        LoanType loanType = LoanType.builder()
                .id(UUID.randomUUID())
                .name("Personal Loan")
                .interestRate(15.5)
                .isAutomaticValidation(true)
                .build();

        when(loanTypeRepository.save(any(LoanType.class))).thenReturn(Mono.just(loanType));

        // Act
        Mono<LoanType> result = loanTypeUseCase.save(loanType);

        // Assert
        StepVerifier.create(result)
                .expectNext(loanType)
                .verifyComplete();

        verify(loanTypeRepository).save(loanType);
    }

    @Test
    void findByIdSuccessfully() {
        // Arrange
        String loanTypeId = UUID.randomUUID().toString();
        LoanType loanType = LoanType.builder()
                .id(UUID.fromString(loanTypeId))
                .name("Vehicle Loan")
                .interestRate(12.0)
                .isAutomaticValidation(false)
                .build();

        when(loanTypeRepository.findById(anyString())).thenReturn(Mono.just(loanType));

        // Act
        Mono<LoanType> result = loanTypeUseCase.findById(loanTypeId);

        // Assert
        StepVerifier.create(result)
                .expectNext(loanType)
                .verifyComplete();

        verify(loanTypeRepository).findById(loanTypeId);
    }

    @Test
    void findByIdNotFound() {
        // Arrange
        String loanTypeId = UUID.randomUUID().toString();

        when(loanTypeRepository.findById(anyString())).thenReturn(Mono.empty());

        // Act
        Mono<LoanType> result = loanTypeUseCase.findById(loanTypeId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(loanTypeRepository).findById(loanTypeId);
    }

    @Test
    void saveHandlesError() {
        // Arrange
        LoanType loanType = LoanType.builder()
                .id(UUID.randomUUID())
                .name("Error Loan")
                .build();

        when(loanTypeRepository.save(any(LoanType.class)))
                .thenReturn(Mono.error(new RuntimeException("Database error")));

        // Act
        Mono<LoanType> result = loanTypeUseCase.save(loanType);

        // Assert
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(loanTypeRepository).save(loanType);
    }

    @Test
    void findByIdHandlesError() {
        // Arrange
        String loanTypeId = UUID.randomUUID().toString();

        when(loanTypeRepository.findById(anyString()))
                .thenReturn(Mono.error(new RuntimeException("Database connection error")));

        // Act
        Mono<LoanType> result = loanTypeUseCase.findById(loanTypeId);

        // Assert
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(loanTypeRepository).findById(loanTypeId);
    }
}