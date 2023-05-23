package me.darkovrbaski.items.marketplace.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.dto.UserDto;
import me.darkovrbaski.items.marketplace.mapper.UserMapper;
import me.darkovrbaski.items.marketplace.model.S3ImageDir;
import me.darkovrbaski.items.marketplace.model.User;
import me.darkovrbaski.items.marketplace.repository.UserRepository;
import me.darkovrbaski.items.marketplace.security.model.RegisterRequest;
import me.darkovrbaski.items.marketplace.service.intefaces.ImageService;
import me.darkovrbaski.items.marketplace.service.intefaces.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {

  UserRepository userRepository;
  UserMapper userMapper;
  PasswordEncoder passwordEncoder;
  ImageService imageService;

  static String USER_NOT_FOUND = "User not found";

  @Override
  public UserDto getUser(final String username) {
    return userMapper.toDto(userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND)));
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
        .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
  }

  @Override
  public UserDto updateUser(final RegisterRequest updateRequest) {
    final var user = userRepository.findByUsername(updateRequest.username())
        .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    user.setEmail(updateRequest.email());
    user.setFirstName(updateRequest.firstName());
    user.setLastName(updateRequest.lastName());
    user.setPhone(updateRequest.phone());
    user.setAddress(updateRequest.address());
    if (updateRequest.password() != null && !updateRequest.password().isEmpty()) {
      user.setPassword(passwordEncoder.encode(updateRequest.password()));
    }
    return userMapper.toDto(userRepository.save(user));
  }

  @Override
  public void updateImage(final MultipartFile file, final String username) {
    final var user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    String imageName = user.getUsername() + "_" + file.getOriginalFilename();
    imageName = imageName.replaceAll("[^a-zA-Z0-9.]", "_");
    user.setImage(imageName);
    imageService.saveImage(file, imageName, S3ImageDir.IMAGES);
    userRepository.save(user);

  }

}
