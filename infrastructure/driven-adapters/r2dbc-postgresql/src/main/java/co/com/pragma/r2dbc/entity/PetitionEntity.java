package co.com.pragma.r2dbc.entity;

import co.com.pragma.model.petition.LoanStatus;
import co.com.pragma.model.petition.Loantypes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetitionEntity {
    @Id
    private UUID id;
    @Column("user_dni")
    private String dni;
    @Column("loan_amount")
    private BigDecimal loanAmount;
    private String term;
    @Column("loan_type")
    private Loantypes loanType;
    @Column("loan_status")
    private LoanStatus loanStatus;
}
