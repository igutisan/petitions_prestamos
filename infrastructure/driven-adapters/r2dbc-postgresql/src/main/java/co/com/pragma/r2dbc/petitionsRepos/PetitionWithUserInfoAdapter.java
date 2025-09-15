package co.com.pragma.r2dbc.petitionsRepos;

import co.com.pragma.model.petitionwithuserinfo.PetitionWithUserInfo;
import co.com.pragma.model.petitionwithuserinfo.gateways.PetitionWithUserInfoRepository;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class PetitionWithUserInfoAdapter extends ReactiveAdapterOperations<
        PetitionWithUserInfo,
        PetitionWithUserInfo,
        String,
        PetitionWithUserInfoReactiveRepository
        > implements PetitionWithUserInfoRepository {

    public PetitionWithUserInfoAdapter(PetitionWithUserInfoReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, PetitionWithUserInfo.class));
    }

    @Override
    public Flux<PetitionWithUserInfo> findAllWithUserInfo(String status, int page, int size) {
        long offset = (long) page * size;

        return repository.findAllWithUserInfo(status, size, offset)
                .map(data -> PetitionWithUserInfo.builder()
                        .id(data.getId())
                        .loanAmount(data.getLoanAmount())
                        .term(data.getTerm())
                        .loanTypeName(data.getLoanTypeName())
                        .loanStatus(data.getLoanStatus())
                        .userEmail(data.getUserEmail())
                        .userName(data.getUserName())
                        .userSalary(data.getUserSalary())
                        .interestRate(data.getInterestRate())
                        .monthlyAmountRequest(data.getMonthlyAmountRequest())
                        .build()
                );
    }

    @Override
    public Mono<PetitionWithUserInfo> findByIdWithUserInfo(UUID id) {
        return repository.findByIdWithUserInfo(id);
    }

    @Override
    public Mono<Long> countByStatus(String status) {
        return repository.countByStatus(status);
    }

    @Override
    public Flux<PetitionWithUserInfo> findAllActiveLoadsWithUserInfo(UUID id) {
        return repository.findAllActiveLoadsWithUserInfo(id);
    }
}
