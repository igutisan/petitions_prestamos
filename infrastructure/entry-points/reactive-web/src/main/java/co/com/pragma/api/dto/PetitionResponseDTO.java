package co.com.pragma.api.dto;

import co.com.pragma.model.petition.LoanStatus;

import java.math.BigDecimal;

public record PetitionResponseDTO(
        String id,
        String dni,
        BigDecimal loanAmount,
        String term,
        String loanTypeName,
        LoanStatus loanStatus
) {
}
