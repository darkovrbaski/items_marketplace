package me.darkovrbaski.items.marketplace.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.dto.ArticleItemDto;
import me.darkovrbaski.items.marketplace.dto.MoneyDto;
import me.darkovrbaski.items.marketplace.dto.OrderDto;
import me.darkovrbaski.items.marketplace.mapper.OrderMapper;
import me.darkovrbaski.items.marketplace.model.ArticleTrade;
import me.darkovrbaski.items.marketplace.model.Order;
import me.darkovrbaski.items.marketplace.model.OrderStatus;
import me.darkovrbaski.items.marketplace.model.Trade;
import me.darkovrbaski.items.marketplace.repository.ArticleRepository;
import me.darkovrbaski.items.marketplace.repository.OrderRepository;
import me.darkovrbaski.items.marketplace.repository.UserRepository;
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
  ArticleRepository articleRepository;
  UserRepository userRepository;

  @Override
  public OrderDto getOrder(final Long id) {
    return orderMapper.toDto(orderRepository.findByIdOrThrow(id));
  }

  @Override
  public List<OrderDto> getActiveOrders(final Long userId) {
    return orderRepository.findAllOpenOrdersByUserId(userId).stream().map(orderMapper::toDto)
        .toList();
  }

  @Override
  public Page<OrderDto> getHistoryOrders(final Long userId, final int page, final int size) {
    return orderRepository.findAllByUserId(userId, PageRequest.of(page, size))
        .map(orderMapper::toDto);
  }

  @Override
  public void deleteOrder(final Long id) {
    final var order = orderRepository.findByIdOrThrow(id);
    order.setStatus(OrderStatus.CLOSED);
    orderRepository.save(order);
  }

  @Override
  public OrderDto createOrder(final OrderDto orderDto) {
    final var order = orderMapper.toEntity(orderDto);
    if (notEnoughItems(order)) {
      throw new IllegalArgumentException("No enough item in inventory");
    }
    if (notEnoughBalance(order)) {
      throw new IllegalArgumentException("Not enough money in wallet");
    }
    final var newOrder = saveOrder(order);
    matchOrders(newOrder);
    return orderMapper.toDto(newOrder);
  }

  private boolean notEnoughItems(final Order order) {
    if (order.isBuyOrder()) {
      return false;
    }
    final var item = inventoryService.searchInventory(
        order.getUser().getId(),
        order.getArticle().getName(),
        0, 1);
    final Optional<ArticleItemDto> firstItem = item.stream().findFirst();
    return firstItem.filter(ai -> ai.quantity().compareTo(order.getQuantity()) >= 0).isEmpty();
  }

  private boolean notEnoughBalance(final Order order) {
    if (order.isSellOrder()) {
      return false;
    }
    final var wallet = walletService.getWallet(order.getUser().getId());
    final var total = order.getPrice().getAmount().multiply(order.getQuantity());
    return wallet.balance().amount().compareTo(total) < 0;
  }

  private Order saveOrder(final Order order) {
    order.setId(0L);
    order.setCreatedDateTime(LocalDateTime.now());
    order.setFilledQuantity(BigDecimal.ZERO);
    order.setStatus(OrderStatus.OPEN);
    order.setTrades(new ArrayList<>());
    order.setArticle(articleRepository.findByIdOrThrow(order.getArticle().getId()));
    order.setUser(userRepository.findByIdOrThrow(order.getUser().getId()));
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
      if (notEnoughItems(matchedOrder) || notEnoughBalance(matchedOrder)) {
        deleteOrder(matchedOrder.getId());
        continue;
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

    updateWalletBalance(trade, newOrder, matchedOrder);
    updateInventory(newOrder, trade, matchedOrder);

    saveOrdersWithTrade(newOrder, matchedOrder, trade);
  }

  private void tradeWithDeficit(final Order newOrder, final Order matchedOrder) {
    final var trade = tradeService.createTrade(newOrder, matchedOrder,
        newOrder.getRemainingQuantity());

    matchedOrder.addFilledQuantity(newOrder.getRemainingQuantity());
    newOrder.setClosedAndFulfilled();

    updateWalletBalance(trade, newOrder, matchedOrder);
    updateInventory(newOrder, trade, matchedOrder);

    saveOrdersWithTrade(newOrder, matchedOrder, trade);
  }

  private void updateInventory(final Order newOrder, final Trade trade, final Order matchedOrder) {
    inventoryService.updateItemInInventory(
        newOrder.isBuyOrder() ? matchedOrder.getUser().getId() : newOrder.getUser().getId(),
        new ArticleTrade(
            trade.getQuantity().negate(),
            newOrder.getArticle()
        )
    );
    inventoryService.updateItemInInventory(
        newOrder.isBuyOrder() ? newOrder.getUser().getId() : matchedOrder.getUser().getId(),
        new ArticleTrade(
            trade.getQuantity(),
            newOrder.getArticle()
        )
    );
  }

  private void updateWalletBalance(final Trade trade, final Order newOrder,
      final Order matchedOrder) {
    walletService.spendFunds(
        newOrder.isBuyOrder() ? newOrder.getUser().getId() : matchedOrder.getUser().getId(),
        new MoneyDto(trade.getTotal(), trade.getPrice().getCurrencyCode())
    );
    walletService.addFunds(
        newOrder.isBuyOrder() ? matchedOrder.getUser().getId() : newOrder.getUser().getId(),
        new MoneyDto(trade.getTotal(), trade.getPrice().getCurrencyCode())
    );
  }

  private void saveOrdersWithTrade(final Order newOrder, final Order matchedOrder,
      final Trade trade) {
    matchedOrder.addTrade(trade);
    newOrder.addTrade(trade);
    orderRepository.saveAll(List.of(matchedOrder, newOrder));
  }

}
