package co.com.pragma.usecase.loantype;

import co.com.pragma.model.loantype.LoanType;
import co.com.pragma.model.loantype.gateways.LoanTypeRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanTypeUseCase {
    private final LoanTypeRepository loanTypeRepository;

    public Mono<LoanType> save(LoanType loanType){
        return loanTypeRepository.save(loanType);
    }

    public Mono<LoanType> findById(String id){
        return loanTypeRepository.findById(id);
    }
}
