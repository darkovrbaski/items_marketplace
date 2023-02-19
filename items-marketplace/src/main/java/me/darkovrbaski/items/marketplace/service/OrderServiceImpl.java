package me.darkovrbaski.items.marketplace.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.dto.MoneyDto;
import me.darkovrbaski.items.marketplace.dto.OrderDto;
import me.darkovrbaski.items.marketplace.mapper.OrderMapper;
import me.darkovrbaski.items.marketplace.model.ArticleTrade;
import me.darkovrbaski.items.marketplace.model.Order;
import me.darkovrbaski.items.marketplace.model.OrderStatus;
import me.darkovrbaski.items.marketplace.model.Trade;
import me.darkovrbaski.items.marketplace.repository.OrderRepository;
import me.darkovrbaski.items.marketplace.service.intefaces.InventoryService;
import me.darkovrbaski.items.marketplace.service.intefaces.OrderService;
import me.darkovrbaski.items.marketplace.service.intefaces.TradeService;
import me.darkovrbaski.items.marketplace.service.intefaces.WalletService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderServiceImpl implements OrderService {

  OrderRepository orderRepository;
  OrderMapper orderMapper;
  TradeService tradeService;
  InventoryService inventoryService;
  WalletService walletService;

  @Override
  public OrderDto getOrder(final Long id) {
    return orderMapper.toDto(orderRepository.findByIdOrThrow(id));
  }

  @Override
  public List<OrderDto> getActiveOrders(final Long userId) {
    return orderRepository.getOpenOrdersByUserId(userId).stream().map(orderMapper::toDto).toList();
  }

  @Override
  public Page<OrderDto> getHistoryOrders(final Long userId, final int page, final int size) {
    return orderRepository.findAllByUserId(userId, PageRequest.of(page, size))
        .map(orderMapper::toDto);
  }

  @Override
  public OrderDto createOrder(final OrderDto orderDto) {
    final var order = saveOrder(orderDto);
    matchOrders(order);
    return orderMapper.toDto(order);
  }

  private Order saveOrder(final OrderDto orderDto) {
    final var order = orderMapper.toEntity(orderDto);
    order.setId(0L);
    order.setCreatedDateTime(LocalDateTime.now());
    order.setFilledQuantity(BigDecimal.ZERO);
    order.setStatus(OrderStatus.OPEN);
    order.setTrades(new ArrayList<>());
    return orderRepository.save(order);
  }

  private void matchOrders(final Order newOrder) {
    final var matchedOrders = newOrder.isBuyOrder()
        ? orderRepository.getOpenSellLowerPricedOlderOrders(newOrder.getPrice().getAmount())
        : orderRepository.getOpenBuyHigherPricedOlderOrders(newOrder.getPrice().getAmount());
    makeTrades(newOrder, matchedOrders);
  }

  private void makeTrades(final Order newOrder, final List<Order> matchedOrders) {
    for (final var matchedOrder : matchedOrders) {
      if (newOrder.isClosed()) {
        return;
      }
      if (hasSufficientQuantity(newOrder, matchedOrder)) {
        tradeWithSurplus(newOrder, matchedOrder);
        continue;
      }
      tradeWithDeficit(newOrder, matchedOrder);
    }
  }

  private static boolean hasSufficientQuantity(final Order newOrder, final Order matchedOrder) {
    return newOrder.getRemainingQuantity().compareTo(matchedOrder.getRemainingQuantity()) >= 0;
  }

  private void tradeWithSurplus(final Order newOrder, final Order matchedOrder) {
    final var trade = tradeService.createTrade(newOrder, matchedOrder,
        matchedOrder.getRemainingQuantity());

    newOrder.addFilledQuantity(matchedOrder.getRemainingQuantity());
    matchedOrder.setClosedAndFulfilled();

    updateWalletBalance(trade);
    updateInventory(newOrder, trade);

    saveOrdersWithTrade(newOrder, matchedOrder, trade);
  }

  private void tradeWithDeficit(final Order newOrder, final Order matchedOrder) {
    final var trade = tradeService.createTrade(newOrder, matchedOrder,
        newOrder.getRemainingQuantity());

    matchedOrder.addFilledQuantity(newOrder.getRemainingQuantity());
    newOrder.setClosedAndFulfilled();

    updateWalletBalance(trade);
    updateInventory(newOrder, trade);

    saveOrdersWithTrade(newOrder, matchedOrder, trade);
  }

  private void updateInventory(final Order newOrder, final Trade trade) {
    inventoryService.updateItemInInventory(
        trade.getBuyOrderId(),
        new ArticleTrade(
            trade.getQuantity(),
            newOrder.getArticle()
        )
    );
    inventoryService.updateItemInInventory(
        trade.getSellOrderId(),
        new ArticleTrade(
            trade.getQuantity().negate(),
            newOrder.getArticle()
        )
    );
  }

  private void updateWalletBalance(final Trade trade) {
    walletService.spendFunds(
        trade.getBuyOrderId(),
        new MoneyDto(trade.getTotal(), trade.getPrice().getCurrencySymbol())
    );
    walletService.addFunds(
        trade.getSellOrderId(),
        new MoneyDto(trade.getTotal(), trade.getPrice().getCurrencySymbol())
    );
  }

  private void saveOrdersWithTrade(final Order newOrder, final Order matchedOrder,
      final Trade trade) {
    matchedOrder.addTrade(trade);
    newOrder.addTrade(trade);
    orderRepository.saveAll(List.of(matchedOrder, newOrder));
  }

}
