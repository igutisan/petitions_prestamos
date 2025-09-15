package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.CreatePetitionDTO;
import co.com.pragma.api.dto.PetitionResponseDTO;
import co.com.pragma.api.dto.UpdateResponseDTO;
import co.com.pragma.model.petition.Petition;
import co.com.pragma.model.petitionwithuserinfo.PetitionWithUserInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetitionWithUserInfoDTOMapper {

    UpdateResponseDTO toDTO(PetitionWithUserInfo petition);

   // Petition toModel(CreatePetitionDTO createPetitionDTO);
}
