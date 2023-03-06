package me.darkovrbaski.items.marketplace.mapper;

import me.darkovrbaski.items.marketplace.config.CentralMapperConfig;
import me.darkovrbaski.items.marketplace.dto.UserDto;
import me.darkovrbaski.items.marketplace.model.User;
import me.darkovrbaski.items.marketplace.security.model.RegisterRequest;
import me.darkovrbaski.items.marketplace.service.intefaces.ImageService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = CentralMapperConfig.class)
public abstract class UserMapper {

  @Autowired
  ImageService imageService;

  @Mapping(
      target = "image",
      expression = "java(imageService.getSignedImageUrl(user.getImage()))"
  )
  public abstract UserDto toDto(User user);

  @Mapping(target = "role", ignore = true)
  public abstract User toEntity(RegisterRequest registerRequest);

}
