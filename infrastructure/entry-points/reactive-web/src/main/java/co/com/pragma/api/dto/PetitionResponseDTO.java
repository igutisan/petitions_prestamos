package co.com.pragma.api.dto;

import co.com.pragma.model.petition.LoanStatus;
import co.com.pragma.model.petition.Loantypes;

import java.math.BigDecimal;

public record PetitionResponseDTO(
        String id,
        String dni,
        BigDecimal loanAmount,
        String term,
        Loantypes loanType,
        LoanStatus loanStatus
) {
}
