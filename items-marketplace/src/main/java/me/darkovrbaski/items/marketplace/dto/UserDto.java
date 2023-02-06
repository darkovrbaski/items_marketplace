package me.darkovrbaski.items.marketplace.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import me.darkovrbaski.items.marketplace.model.Address;

public record UserDto(

    Long id,

    @Size(min = 3, message = "Username must be at least 3 characters long")
    String username,

    @Size(min = 3, message = "Password must be at least 3 characters long")
    String password,

    String firstName,

    String lastName,

    @Email(message = "Email should be valid")
    String email,

    String phone,

    String image,

    @PositiveOrZero(message = "Wallet balance must be positive or zero")
    BigDecimal walletBalance,

    boolean administrator,

    Address address

) {

}