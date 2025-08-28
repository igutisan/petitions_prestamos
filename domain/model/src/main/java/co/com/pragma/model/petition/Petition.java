package co.com.pragma.model.petition;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Petition {
    private UUID id;
    private String dni;
    private BigDecimal loanAmount;
    private String term;
    private Loantypes loanType;
    private LoanStatus loanStatus;

}
