package me.darkovrbaski.items.marketplace.service.intefaces;

import me.darkovrbaski.items.marketplace.dto.UserDto;
import me.darkovrbaski.items.marketplace.model.User;
import me.darkovrbaski.items.marketplace.security.model.RegisterRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  UserDto getUser(String username);

  Page<UserDto> getUsers(int page, int size);

  Page<UserDto> searchUsers(String username, int page, int size);

  void deleteUser(Long userId);

  User findByUsername(String name);

  UserDto updateUser(RegisterRequest updateRequest);

  void updateImage(MultipartFile file, String username);
  
}
