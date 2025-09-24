package co.com.pragma.usecase.petition;


import co.com.pragma.model.client.Client;
import co.com.pragma.model.client.gateways.ClientRepository;
import co.com.pragma.model.exceptions.NotFoundException;
import co.com.pragma.model.loantype.LoanType;
import co.com.pragma.model.loantype.gateways.LoanTypeRepository;
import co.com.pragma.model.petition.Petition;
import co.com.pragma.model.petition.gateways.MessageQueueGateway;
import co.com.pragma.model.petition.gateways.PetitionRepository;
import co.com.pragma.model.petitionwithuserinfo.PetitionWithUserInfo;
import co.com.pragma.model.petitionwithuserinfo.gateways.PetitionWithUserInfoRepository;
import co.com.pragma.usecase.petition.dto.LambdaRequestDTO;
import co.com.pragma.usecase.petition.dto.PetitionActualWithUserInfo;
import co.com.pragma.usecase.petition.dto.PetitionStatusMessage;
import co.com.pragma.usecase.petition.dto.ValidationResponseDTO;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
public class PetitionUseCase {

    private final PetitionRepository petitionRepository;
    private final PetitionWithUserInfoRepository petitionWithUserInfoRepository;
    private final ClientRepository clientRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final MessageQueueGateway messageQueueGateway;


    // Writes
    public Mono<Petition> createPetition(Petition petition) {
        Mono<LoanType> loanTypeMono = loanTypeRepository.findById(String.valueOf(petition.getLoanTypeId()))
                .switchIfEmpty(Mono.error(new NotFoundException("El tipo de préstamo con ID "
                        + petition.getLoanTypeId() + " no fue encontrado.")))
                .cache();

        Mono<Client> clientMono = clientRepository.findById(String.valueOf(petition.getUserId()))
                        .switchIfEmpty(Mono.error(new NotFoundException("El cliente con ID "
                                + petition.getUserId() + " no fue encontrado."))).cache();

        Mono<Petition> savedPetitionMono = petitionRepository.save(petition).cache();


        return loanTypeMono.flatMap(loanType -> savedPetitionMono.flatMap(savedPetition -> {

            if (!loanType.isAutomaticValidation()) {
                return Mono.just(savedPetition);
            }

            return petitionWithUserInfoRepository.findAllActiveLoadsWithUserInfo(savedPetition.getUserId())
                    .collectList() // <-- Materializa el Flux en un Mono<List<...>>
                    .flatMap(activeLoadsList ->
                            clientMono.flatMap(client -> {
                            LambdaRequestDTO lambdaRequestDTO = LambdaRequestDTO.builder()
                                    .petitionActualWithUserInfo(PetitionActualWithUserInfo.builder()
                                            .userSalary(client.getSalary())
                                            .userEmail(client.getEmail())
                                            .term(savedPetition.getTerm())
                                            .userName(client.getNames()+ " "+client.getLastNames())
                                            .loanAmount(savedPetition.getLoanAmount())
                                            .id(savedPetition.getId())
                                            .build())
                                    .petitionWithUserInfoList(activeLoadsList)
                                    .build();

                        return messageQueueGateway.sendMessageToAutomaticValidation(lambdaRequestDTO)
                                .thenReturn(savedPetition);
                    }));
        }));
    }

    public Mono<PetitionWithUserInfo> updatePetitionStatus(ValidationResponseDTO validationResponseDTO) {


        return petitionRepository.findById(String.valueOf(validationResponseDTO.getPetitionId()))
                .switchIfEmpty(Mono.error(new NotFoundException("La petición con ID " + validationResponseDTO.getPetitionId() + " no fue encontrada.")))
                .flatMap(existingPetition -> {
                    existingPetition.setLoanStatus(validationResponseDTO.getStatus());
                    return petitionRepository.save(existingPetition);
                })
                .flatMap(updatedPetition -> petitionWithUserInfoRepository.findByIdWithUserInfo(updatedPetition.getId()))
                .flatMap(petitionWithInfo -> {
                    PetitionStatusMessage message = new PetitionStatusMessage(
                            petitionWithInfo.getId(),
                            petitionWithInfo.getUserName(),
                            petitionWithInfo.getLoanStatus().toString(),
                            petitionWithInfo.getUserEmail(),
                            petitionWithInfo.getTerm(),
                            petitionWithInfo.getLoanAmount(),
                            petitionWithInfo.getInterestRate()

                    );

                    Mono<Void> acceptedQueueMono;

                    if ("APPROVED".equalsIgnoreCase(petitionWithInfo.getLoanStatus().toString())) {
                        acceptedQueueMono = messageQueueGateway.sendMessageToAcceptedPetitionsQueue(message);
                    } else {
                        acceptedQueueMono = Mono.empty();
                    }

                    return acceptedQueueMono
                            .then(messageQueueGateway.sendMessageToNotificationQueue(message))
                            .thenReturn(petitionWithInfo);

                });
    }


    // Reads

    public Flux<PetitionWithUserInfo> getAllPetitionsWithUserInfo(String status, int page, int size) {
        return petitionWithUserInfoRepository.findAllWithUserInfo(status, page, size)
                .map(data -> {
                    data.setMonthlyAmountRequest(
                            monthlyAmountRequest(
                                    data.getInterestRate(),
                                    data.getLoanAmount(),
                                    data.getTerm()
                            )
                    );
                    return data;
                });
    }


    public Mono<Long> countByStatus(String status){
        return petitionWithUserInfoRepository.countByStatus(status);
    }

    //Auxiliary methods

    private BigDecimal monthlyAmountRequest(double annualInterestRate, BigDecimal amount, int months) {

        double monthlyRate = Math.pow(1 + (annualInterestRate / 100.0), 1.0/12.0) - 1;

        if (monthlyRate == 0) {
            return amount.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
        }

        double pow = Math.pow(1 + monthlyRate, -months);

        double cuota = (amount.doubleValue() * monthlyRate) / (1 - pow);
        return BigDecimal.valueOf(cuota).setScale(2, RoundingMode.HALF_UP);
    }

}
