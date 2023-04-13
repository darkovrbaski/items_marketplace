package me.darkovrbaski.items.marketplace.mapper;

import me.darkovrbaski.items.marketplace.config.CentralMapperConfig;
import me.darkovrbaski.items.marketplace.dto.OrderDto;
import me.darkovrbaski.items.marketplace.model.Order;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class, uses = {UserMapper.class, ArticleMapper.class,
    WalletMapper.class})
public interface OrderMapper {

  OrderDto toDto(Order order);

  Order toEntity(OrderDto orderDto);

}
