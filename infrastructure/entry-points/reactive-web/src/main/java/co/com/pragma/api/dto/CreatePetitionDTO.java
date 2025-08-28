package co.com.pragma.api.dto;

import co.com.pragma.model.petition.Loantypes;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreatePetitionDTO(
        @NotBlank(message = "El DNI no puede estar vacío")
        @Size(min = 6, max = 15, message = "El DNI debe tener entre 6 y 15 caracteres")
        String dni,

        @NotNull(message = "El monto del préstamo es obligatorio")
        @DecimalMin(value = "500000.00", message = "El monto mínimo permitido es $500,000 COP")
        @Digits(integer = 12, fraction = 0, message = "El monto debe ser un valor entero en pesos, sin decimales")
        BigDecimal loanAmount,

        @NotBlank(message = "El plazo no puede estar vacío")
        @Pattern(regexp = "^(6|12|24|36|48|60)$", message = "El plazo debe ser uno de: 6, 12, 24, 36, 48 o 60 meses")
        String term,

        @NotNull(message = "El tipo de préstamo es obligatorio")
        Loantypes loanType
) {
}
