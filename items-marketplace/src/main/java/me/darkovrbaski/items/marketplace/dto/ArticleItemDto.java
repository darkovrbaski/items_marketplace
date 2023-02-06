package me.darkovrbaski.items.marketplace.dto;

import java.math.BigDecimal;

public record ArticleItemDto(

    BigDecimal quantity,

    ArticleDto article
    
) {

}