package me.darkovrbaski.items.marketplace.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.model.AccumulatedOrder;
import me.darkovrbaski.items.marketplace.model.Money;
import me.darkovrbaski.items.marketplace.model.Order;
import me.darkovrbaski.items.marketplace.model.OrderBook;
import me.darkovrbaski.items.marketplace.model.OrderType;
import me.darkovrbaski.items.marketplace.repository.OrderRepository;
import me.darkovrbaski.items.marketplace.service.intefaces.OrderBookService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderBookServiceImpl implements OrderBookService {

  OrderRepository orderRepository;

  @Override
  public OrderBook getOrderBook(final Long articleId) {
    return new OrderBook(
        getAccumulatedSellOrders(articleId),
        getAccumulatedBuyOrders(articleId)
    );
  }

  private List<AccumulatedOrder> getAccumulatedBuyOrders(final Long articleId) {
    final var buyOrders = orderRepository.getOpenBuyOrdersByArticleIdAndType(articleId,
        OrderType.BUY);
    return getAccumulatedOrders(buyOrders).stream()
        .sorted(Comparator.comparing(ao -> ((AccumulatedOrder) ao).price().getAmount()).reversed())
        .toList();
  }

  private List<AccumulatedOrder> getAccumulatedSellOrders(final Long articleId) {
    final var sellOrders = orderRepository.getOpenBuyOrdersByArticleIdAndType(articleId,
        OrderType.SELL);
    return getAccumulatedOrders(sellOrders).stream()
        .sorted(Comparator.comparing(ao -> ao.price().getAmount()))
        .toList();
  }

  private static List<AccumulatedOrder> getAccumulatedOrders(final List<Order> sellOrders) {
    return sellOrders.stream()
        .collect(Collectors.groupingBy(o -> o.getPrice().getAmount(),
            Collectors.reducing(BigDecimal.ZERO, Order::getRemainingQuantity, BigDecimal::add)))
        .entrySet().stream()
        .map(e -> new AccumulatedOrder(Money.dollars(e.getKey()), e.getValue())).toList();
  }

}
