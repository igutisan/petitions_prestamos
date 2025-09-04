package co.com.pragma.model.petitionwithuserinfo;
import co.com.pragma.model.petition.LoanStatus;
import co.com.pragma.model.petition.Loantypes;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PetitionWithUserInfo {
    private BigDecimal loanAmount;
    private String term;
    private Loantypes loanType;
    private LoanStatus loanStatus;

    private String userEmail;
    private String userName;
    private BigDecimal userSalary;
}
