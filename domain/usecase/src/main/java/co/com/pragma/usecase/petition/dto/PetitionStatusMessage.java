package co.com.pragma.usecase.petition.dto;

import java.math.BigDecimal;

public record PetitionStatusMessage(
        String client,
        String status,
        String email,
        int term,
        BigDecimal loanAmount,
        double interestRate
) {
}
