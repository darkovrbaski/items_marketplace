package me.darkovrbaski.items.marketplace.model;

import java.math.BigDecimal;
import lombok.Value;

@Value
public class AccumulatedOrders {

  BigDecimal price;

  BigDecimal quantity;

}