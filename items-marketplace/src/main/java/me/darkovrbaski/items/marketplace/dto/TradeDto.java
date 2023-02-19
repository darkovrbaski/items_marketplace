package me.darkovrbaski.items.marketplace.dto;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TradeDto(

    Long id,

    LocalDateTime createdDateTime,

    MoneyDto price,

    @Positive
    BigDecimal quantity,

    Long sellOrderId,

    Long buyOrderId

) {

}
