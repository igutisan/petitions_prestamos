package co.com.pragma.api.dto;



import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateClientDTO(

        UUID id,

        String names,

        String lastNames,

        LocalDate dob,

        String phone,


        String address,


        String email,


        String dni,


        BigDecimal salary


) {
}
