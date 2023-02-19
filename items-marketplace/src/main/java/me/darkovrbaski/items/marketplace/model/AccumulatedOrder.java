package me.darkovrbaski.items.marketplace.model;

import java.math.BigDecimal;

public record AccumulatedOrder(

    Money price,

    BigDecimal quantity

) {

}