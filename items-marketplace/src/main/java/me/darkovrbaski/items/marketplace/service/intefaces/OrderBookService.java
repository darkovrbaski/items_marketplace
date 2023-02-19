package me.darkovrbaski.items.marketplace.service.intefaces;

import me.darkovrbaski.items.marketplace.model.OrderBook;

public interface OrderBookService {

  OrderBook getOrderBook(Long articleId);

}
