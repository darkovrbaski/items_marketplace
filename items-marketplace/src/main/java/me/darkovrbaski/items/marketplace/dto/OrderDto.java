package me.darkovrbaski.items.marketplace.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import me.darkovrbaski.items.marketplace.model.OrderStatus;
import me.darkovrbaski.items.marketplace.model.OrderType;

public record OrderDto(

    Long id,

    LocalDateTime createdDateTime,

    OrderType type,

    MoneyDto price,

    @Positive
    BigDecimal quantity,

    @PositiveOrZero
    BigDecimal filledQuantity,

    OrderStatus status,

    List<TradeDto> trades,

    UserDto user,

    ArticleDto article

) {

}
