package co.com.pragma.model.petitionwithuserinfo;
import co.com.pragma.model.petition.LoanStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PetitionWithUserInfo {
    private String id;
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
