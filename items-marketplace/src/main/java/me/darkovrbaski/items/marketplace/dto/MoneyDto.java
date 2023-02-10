package me.darkovrbaski.items.marketplace.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import org.hibernate.validator.constraints.Length;

public record MoneyDto(

    @PositiveOrZero(message = "Amount must be positive or zero")
    BigDecimal amount,

    @Length(min = 3, max = 3, message = "Currency must be 3 characters long")
    @Pattern(regexp = "USD", message = "Currency must be USD")
    String currency

) {

}
