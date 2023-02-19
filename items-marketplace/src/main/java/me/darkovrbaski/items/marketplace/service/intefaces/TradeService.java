package me.darkovrbaski.items.marketplace.service.intefaces;

import java.math.BigDecimal;
import me.darkovrbaski.items.marketplace.model.Order;
import me.darkovrbaski.items.marketplace.model.Trade;

public interface TradeService {

  Trade createTrade(Order newOrder, Order matchedOrder, BigDecimal subtract);

}
