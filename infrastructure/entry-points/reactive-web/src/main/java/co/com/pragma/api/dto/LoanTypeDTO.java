
package co.com.pragma.api.dto;

public record LoanTypeDTO(
        String name,
        double interestRate,
        boolean isAutomaticValidation
) {
}
