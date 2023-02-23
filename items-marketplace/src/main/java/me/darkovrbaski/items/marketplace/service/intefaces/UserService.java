package me.darkovrbaski.items.marketplace.service.intefaces;

import me.darkovrbaski.items.marketplace.dto.UserDto;
import me.darkovrbaski.items.marketplace.model.User;
import org.springframework.data.domain.Page;

public interface UserService {

  UserDto getUser(Long userId);

  Page<UserDto> getUsers(int page, int size);

  Page<UserDto> searchUsers(String username, int page, int size);

  void deleteUser(Long userId);

  User findByUsername(String name);

}
