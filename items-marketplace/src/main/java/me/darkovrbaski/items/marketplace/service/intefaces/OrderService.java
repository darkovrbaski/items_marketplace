package me.darkovrbaski.items.marketplace.service.intefaces;

import java.util.List;
import me.darkovrbaski.items.marketplace.dto.OrderDto;
import org.springframework.data.domain.Page;

public interface OrderService {

  OrderDto getOrder(Long id);

  List<OrderDto> getActiveOrders(Long userId);

  Page<OrderDto> getHistoryOrders(Long userId, int page, int size);

  void deleteOrder(Long id);
  
  OrderDto createOrder(OrderDto orderDto);

}
