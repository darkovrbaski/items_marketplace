package me.darkovrbaski.items.marketplace.mapper;

import me.darkovrbaski.items.marketplace.config.CentralMapperConfig;
import me.darkovrbaski.items.marketplace.dto.OrderDto;
import me.darkovrbaski.items.marketplace.model.Order;
import me.darkovrbaski.items.marketplace.service.intefaces.ImageService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = CentralMapperConfig.class)
public abstract class OrderMapper {

  @Autowired
  ImageService imageService;

  @Mapping(
      target = "user.image",
      expression = "java(imageService.getSignedImageUrl(user.getImage()))"
  )
  @Mapping(
      target = "article.image",
      expression = "java(imageService.getPublicImageUrl(article.getImage()))"
  )
  public abstract OrderDto toDto(Order order);

  @Mapping(target = "user.password", ignore = true)
  public abstract Order toEntity(OrderDto orderDto);

}
