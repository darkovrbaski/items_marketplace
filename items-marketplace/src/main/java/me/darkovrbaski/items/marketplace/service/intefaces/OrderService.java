package me.darkovrbaski.items.marketplace.service.intefaces;

import java.util.List;
import me.darkovrbaski.items.marketplace.dto.OrderDto;
import org.springframework.data.domain.Page;

public interface OrderService {

  OrderDto getOrder(Long id);

  OrderDto getUserOrder(Long id, String username);

  List<OrderDto> getActiveOrders(Long userId);

  Page<OrderDto> getHistoryOrders(Long userId, int page, int size);

  void closeOrder(Long id);

  OrderDto trade(OrderDto orderDto, Long matchedOrderId);

  OrderDto createOrder(OrderDto orderDto);

  Page<OrderDto> getMatchedSellOrders(OrderDto orderDto, int page, int size);

}
