package co.com.pragma.r2dbc.petitionsRepos;

import co.com.pragma.model.petitionwithuserinfo.PetitionWithUserInfo;
import co.com.pragma.model.petitionwithuserinfo.gateways.PetitionWithUserInfoRepository;
import co.com.pragma.r2dbc.dto.PetitionWithUserInfoData;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class PetitionWithUserInfoAdapter extends ReactiveAdapterOperations<
        PetitionWithUserInfo,
        PetitionWithUserInfoData,
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
                        .loanAmount(data.getLoanAmount())
                        .term(data.getTerm())
                        .loanType(data.getLoanType())
                        .loanStatus(data.getLoanStatus())
                        .userEmail(data.getUserEmail())
                        .userName(data.getUserName())
                        .userSalary(data.getUserSalary())
                        .build()
                );
    }
}
