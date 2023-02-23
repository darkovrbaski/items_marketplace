package me.darkovrbaski.items.marketplace.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.dto.UserDto;
import me.darkovrbaski.items.marketplace.mapper.UserMapper;
import me.darkovrbaski.items.marketplace.model.User;
import me.darkovrbaski.items.marketplace.repository.UserRepository;
import me.darkovrbaski.items.marketplace.service.intefaces.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {

  UserRepository userRepository;
  UserMapper userMapper;

  @Override
  public UserDto getUser(final Long userId) {
    return userMapper.toDto(userRepository.findByIdOrThrow(userId));
  }

  @Override
  public Page<UserDto> getUsers(final int page, final int size) {
    return userRepository.findAll(PageRequest.of(page, size)).map(userMapper::toDto);
  }

  @Override
  public Page<UserDto> searchUsers(final String username, final int page, final int size) {
    return userRepository.findByUsernameContainingIgnoreCase(username, PageRequest.of(page, size))
        .map(userMapper::toDto);
  }

  @Override
  public void deleteUser(final Long userId) {
    userRepository.deleteById(userId);
  }

  @Override
  public User findByUsername(final String name) {
    return userRepository.findByUsername(name)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

}
