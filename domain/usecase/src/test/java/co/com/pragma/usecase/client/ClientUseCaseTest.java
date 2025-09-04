
package co.com.pragma.usecase.client;

import co.com.pragma.model.client.Client;
import co.com.pragma.model.client.gateways.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientUseCaseTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientUseCase clientUseCase;

    @Test
    void createClientSuccessfully() {
        // Arrange
        Client client = Client.builder()
                .id(UUID.randomUUID())
                .names("Test")
                .lastNames("User")
                .dni("12345")
                .dob(LocalDate.now().minusYears(20))
                .phone("1234567890")
                .address("123 Test St")
                .email("test@user.com")
                .salary(new BigDecimal("50000"))
                .build();

        when(clientRepository.insertUser(any(Client.class))).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = clientUseCase.createClient(client);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();
    }
}
