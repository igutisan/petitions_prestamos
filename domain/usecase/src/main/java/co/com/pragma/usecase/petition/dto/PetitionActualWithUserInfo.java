package co.com.pragma.usecase.petition.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PetitionActualWithUserInfo {

    private UUID id;
    private BigDecimal loanAmount;
    private int term;
    private BigDecimal userSalary;
    private String userEmail;
    private String userName;
}
