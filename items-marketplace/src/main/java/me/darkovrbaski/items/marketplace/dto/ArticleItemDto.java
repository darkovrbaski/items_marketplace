package me.darkovrbaski.items.marketplace.dto;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ArticleItemDto(

    @Positive(message = "Quantity must be positive")
    BigDecimal quantity,

    ArticleDto article

) {

}