package co.com.pragma.api.dto;

import java.util.UUID;

public record LoanTypeResponseDTO(
        UUID id,
        String name,
        double interestRate,
        boolean isAutomaticValidation
) {
}
