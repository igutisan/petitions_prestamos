package co.com.pragma.usecase.petition;


import co.com.pragma.model.petition.Petition;
import co.com.pragma.model.petition.gateways.PetitionRepository;
import co.com.pragma.model.petitionwithuserinfo.PetitionWithUserInfo;
import co.com.pragma.model.petitionwithuserinfo.gateways.PetitionWithUserInfoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class PetitionUseCase {


    private final PetitionRepository petitionRepository;
    private final PetitionWithUserInfoRepository petitionWithUserInfoRepository;


    public Mono<Petition> createPetition(Petition petition) {

        return petitionRepository.save(petition);
    }

    public Flux<PetitionWithUserInfo> getAllPetitionsWithUserInfo(String status, int page, int size) {
        return petitionWithUserInfoRepository.findAllWithUserInfo(status,page,size);

    }
}
