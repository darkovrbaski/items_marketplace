package me.darkovrbaski.items.marketplace.mapper;

import me.darkovrbaski.items.marketplace.dto.UserDto;
import me.darkovrbaski.items.marketplace.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

  UserDto toDto(User user);

}
