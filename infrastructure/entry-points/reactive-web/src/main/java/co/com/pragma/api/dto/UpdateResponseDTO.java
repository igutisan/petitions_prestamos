package co.com.pragma.api.dto;

import co.com.pragma.model.petition.LoanStatus;

import java.math.BigDecimal;

public record UpdateResponseDTO(
         BigDecimal loanAmount,
         String loanTypeName,
         LoanStatus loanStatus,
         String userEmail,
         String userName

) {
}
