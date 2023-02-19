package me.darkovrbaski.items.marketplace.mapper;

import me.darkovrbaski.items.marketplace.dto.OrderDto;
import me.darkovrbaski.items.marketplace.model.Order;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {

  OrderDto toDto(Order order);

  Order toEntity(OrderDto orderDto);

}
