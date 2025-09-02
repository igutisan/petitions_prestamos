package co.com.pragma.model.client;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Client {
    private UUID id;
    private String names;
    private String lastNames;
    private String dni;
    private LocalDate dob;
    private String phone;
    private String address;
    private String email;
    private BigDecimal salary;
}
