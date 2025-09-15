package co.com.pragma.usecase.petition.dto;

import co.com.pragma.model.petition.LoanStatus;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ValidationResponseDTO {
    String petitionId;
    LoanStatus status;
}
