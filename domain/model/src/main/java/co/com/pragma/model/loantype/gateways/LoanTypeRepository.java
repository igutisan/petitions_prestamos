
package co.com.pragma.model.loantype.gateways;

import co.com.pragma.model.loantype.LoanType;

import reactor.core.publisher.Mono;

public interface LoanTypeRepository {
    Mono<LoanType> save(LoanType loanType);
    Mono<LoanType> findById(String id);

}
