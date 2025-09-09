package co.com.pragma.model.petitionwithuserinfo;
import co.com.pragma.model.petition.LoanStatus;
import co.com.pragma.model.petition.Loantypes;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PetitionWithUserInfo {
    private BigDecimal loanAmount;
    private int term;
    private String loanTypeName;
    private LoanStatus loanStatus;
    private String userEmail;
    private String userName;
    private double interestRate;
    private BigDecimal monthlyAmountRequest;
    private BigDecimal userSalary;
}
