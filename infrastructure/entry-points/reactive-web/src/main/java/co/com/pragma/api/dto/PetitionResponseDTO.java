package co.com.pragma.api.dto;

import co.com.pragma.model.petition.LoanStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record PetitionResponseDTO(
        String id,
        String dni,
        BigDecimal loanAmount,
        int term,
        UUID loanTypeId,
        LoanStatus loanStatus
) {
}
