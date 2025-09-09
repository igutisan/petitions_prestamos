package co.com.pragma.usecase.petition;


import co.com.pragma.model.exceptions.NotFoundException;
import co.com.pragma.model.loantype.gateways.LoanTypeRepository;
import co.com.pragma.model.petition.Petition;
import co.com.pragma.model.petition.gateways.PetitionRepository;
import co.com.pragma.model.petitionwithuserinfo.PetitionWithUserInfo;
import co.com.pragma.model.petitionwithuserinfo.gateways.PetitionWithUserInfoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;


@RequiredArgsConstructor
public class PetitionUseCase {


    private final PetitionRepository petitionRepository;
    private final PetitionWithUserInfoRepository petitionWithUserInfoRepository;
    private final LoanTypeRepository loanTypeRepository;


    public Mono<Petition> createPetition(Petition petition) {
        return loanTypeRepository.findById(String.valueOf(petition.getLoanTypeId()))
                .flatMap(loanTypeFound -> petitionRepository.save(petition))
                .switchIfEmpty(Mono.error(new NotFoundException("El tipo de préstamo con ID " + petition.getLoanTypeId() + " no fue encontrado.")));
    }

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
