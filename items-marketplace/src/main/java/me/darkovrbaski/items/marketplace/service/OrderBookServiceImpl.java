package me.darkovrbaski.items.marketplace.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.model.OrderBook;
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
        orderRepository.getOpenSellOrdersByArticleId(articleId),
        orderRepository.getOpenBuyOrdersByArticleId(articleId)
    );
  }

}
