package co.com.pragma.model.petitionwithuserinfo.gateways;

import co.com.pragma.model.petitionwithuserinfo.PetitionWithUserInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PetitionWithUserInfoRepository {
    Flux<PetitionWithUserInfo> findAllWithUserInfo(String status, int page, int size);
    Mono<Long>countByStatus(String status);
}
