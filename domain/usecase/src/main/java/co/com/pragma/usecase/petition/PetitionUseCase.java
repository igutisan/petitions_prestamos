package co.com.pragma.usecase.petition;

import co.com.pragma.model.petition.Petition;
import co.com.pragma.model.petition.gateways.AuthenticationGateway;
import co.com.pragma.model.petition.gateways.LoggerGateway;
import co.com.pragma.model.petition.gateways.PetitionRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class PetitionUseCase {

    private final AuthenticationGateway clientGateway;
    private final PetitionRepository petitionRepository;
    private final LoggerGateway loggerGateway;

    public Mono<Petition> createPetition(Petition petition) {

//        return clientGateway.validateUser(petition.getUserId())
//                .flatMap(exist -> {
//                    loggerGateway.logInfo("Valdiating user");
//                    if (exist) {
//                        petition.setLoanStatus(LoanStatus.PENDING_REVIEW);
//                        return petitionRepository.save(petition);
//                    } else {
//                        return Mono.error(new InvalidUser("Cliente no existe"));
//                    }
//                });
        return petitionRepository.save(petition);
    }
}
