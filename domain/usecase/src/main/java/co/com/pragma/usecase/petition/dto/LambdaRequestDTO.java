package co.com.pragma.usecase.petition.dto;


import co.com.pragma.model.petition.LoanStatus;
import co.com.pragma.model.petition.Petition;
import co.com.pragma.model.petitionwithuserinfo.PetitionWithUserInfo;
import lombok.*;
import reactor.core.publisher.Flux;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LambdaRequestDTO{
    private  PetitionActualWithUserInfo petitionActualWithUserInfo;
    private List<PetitionWithUserInfo> petitionWithUserInfoList;
}

