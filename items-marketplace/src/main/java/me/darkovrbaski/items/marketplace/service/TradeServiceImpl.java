package me.darkovrbaski.items.marketplace.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.model.Money;
import me.darkovrbaski.items.marketplace.model.Order;
import me.darkovrbaski.items.marketplace.model.Trade;
import me.darkovrbaski.items.marketplace.repository.TradeRepository;
import me.darkovrbaski.items.marketplace.service.intefaces.TradeService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TradeServiceImpl implements TradeService {

  TradeRepository tradeRepository;

  @Override
  public Trade createTrade(final Order newOrder, final Order matchedOrder,
      final BigDecimal quantity) {
    final var trade = Trade.builder()
        .price(calculatePrice(newOrder, matchedOrder))
        .quantity(quantity)
        .createdDateTime(LocalDateTime.now())
        .buyOrderId(newOrder.isBuyOrder() ? newOrder.getId() : matchedOrder.getId())
        .sellOrderId(newOrder.isBuyOrder() ? matchedOrder.getId() : newOrder.getId())
        .build();
    return tradeRepository.save(trade);
  }

  private Money calculatePrice(final Order order, final Order matchedOrder) {
    if (order.isBuyOrder() && !order.isEnabledAutoTrade()) {
      return matchedOrder.getLowerSellPrice();
    }
    return matchedOrder.getPrice();
  }

}
