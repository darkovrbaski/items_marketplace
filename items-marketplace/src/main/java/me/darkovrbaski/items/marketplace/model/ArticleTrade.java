package me.darkovrbaski.items.marketplace.model;

import java.math.BigDecimal;

public record ArticleTrade(

    BigDecimal quantity,

    Article article

) {

}
