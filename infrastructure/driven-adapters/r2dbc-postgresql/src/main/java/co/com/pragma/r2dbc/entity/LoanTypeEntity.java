
package co.com.pragma.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "loan_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanTypeEntity {
    @Id
    private UUID id;

    @Column("name")
    private String name;

    @Column("interest_rate")
    private Double interestRate;

    @Column("is_automatic_validation")
    private boolean isAutomaticValidation;
}
