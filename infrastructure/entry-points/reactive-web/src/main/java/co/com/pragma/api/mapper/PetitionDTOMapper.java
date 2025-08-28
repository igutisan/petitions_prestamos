package co.com.pragma.api.mapper;


import co.com.pragma.api.dto.CreatePetitionDTO;
import co.com.pragma.api.dto.PetitionResponseDTO;
import co.com.pragma.model.petition.Petition;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetitionDTOMapper {

    PetitionResponseDTO toDTO(Petition petition);

    Petition toModel(CreatePetitionDTO createPetitionDTO);
}
