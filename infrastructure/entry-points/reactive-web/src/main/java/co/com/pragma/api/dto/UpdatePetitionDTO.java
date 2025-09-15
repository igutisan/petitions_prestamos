package co.com.pragma.api.dto;

import co.com.pragma.model.petition.LoanStatus;
import jakarta.validation.constraints.NotNull;

public record UpdatePetitionDTO(
        @NotNull(message = "El estado del préstamo no puede ser nulo")
        LoanStatus status
) {
}
