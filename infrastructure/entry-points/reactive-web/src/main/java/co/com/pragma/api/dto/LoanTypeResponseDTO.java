package co.com.pragma.api.dto;

public record LoanTypeResponseDTO(
        String id,
        String name,
        Double interestRate
) {
}
