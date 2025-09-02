package co.com.pragma.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntity {
    @Id
    private UUID id;

    @Column("names")
    private String names;

    @Column("last_names")
    private String lastNames;

    @Column("dni")
    private String dni;

    @Column("dob")
    private LocalDate dob;

    @Column("phone")
    private String phone;

    @Column("address")
    private String address;

    @Column("email")
    private String email;

    @Column("salary")
    private BigDecimal salary;


}
