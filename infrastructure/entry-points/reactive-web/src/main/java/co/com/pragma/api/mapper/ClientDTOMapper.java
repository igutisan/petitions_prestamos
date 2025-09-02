package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.CreateClientDTO;
import co.com.pragma.model.client.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientDTOMapper {
   // PetitionResponseDTO toDTO(Petition petition);

    Client toModel(CreateClientDTO client);
}
