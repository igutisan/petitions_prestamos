package co.com.pragma.model.petitionwithuserinfo.gateways;

import co.com.pragma.model.petitionwithuserinfo.PetitionWithUserInfo;
import reactor.core.publisher.Flux;

public interface PetitionWithUserInfoRepository {
    Flux<PetitionWithUserInfo> findAllWithUserInfo(String status, int page, int size);
}
