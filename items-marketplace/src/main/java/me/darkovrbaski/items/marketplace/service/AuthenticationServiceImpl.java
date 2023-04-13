package me.darkovrbaski.items.marketplace.service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.dto.UserDto;
import me.darkovrbaski.items.marketplace.mapper.UserMapper;
import me.darkovrbaski.items.marketplace.model.Address;
import me.darkovrbaski.items.marketplace.model.Role;
import me.darkovrbaski.items.marketplace.model.User;
import me.darkovrbaski.items.marketplace.repository.UserRepository;
import me.darkovrbaski.items.marketplace.security.JwtUtil;
import me.darkovrbaski.items.marketplace.security.model.AuthRequest;
import me.darkovrbaski.items.marketplace.security.model.AuthResponse;
import me.darkovrbaski.items.marketplace.security.model.RegisterRequest;
import me.darkovrbaski.items.marketplace.service.intefaces.AuthenticationService;
import me.darkovrbaski.items.marketplace.service.intefaces.InventoryService;
import me.darkovrbaski.items.marketplace.service.intefaces.WalletService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthenticationServiceImpl implements AuthenticationService {

  UserRepository userRepository;
  UserMapper userMapper;
  PasswordEncoder passwordEncoder;
  JwtUtil jwtUtil;
  AuthenticationManager authManager;
  WalletService walletService;
  InventoryService inventoryService;

  @Override
  public AuthResponse login(final AuthRequest authRequest) {
    authManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            authRequest.username(),
            authRequest.password()
        )
    );
    final var user = userRepository.findByUsername(authRequest.username())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    final var jwtToken = jwtUtil.generateToken(user);
    return new AuthResponse(user.getUsername(), jwtToken, jwtUtil.extractExpiration(jwtToken));
  }

  @Transactional
  @Override
  public AuthResponse register(final RegisterRequest registerRequest) {
    final User user = createUser(registerRequest);
    walletService.createWallet(user);
    inventoryService.createInventory(user);
    return login(new AuthRequest(registerRequest.username(), registerRequest.password()));
  }

  private User createUser(final RegisterRequest registerRequest) {
    final var user = userMapper.toEntity(registerRequest);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole(Role.ROLE_USER);
    user.setAddress(new Address("", "", "", ""));
    user.setPhone("");
    return userRepository.save(user);
  }

  @Override
  public UserDto registerAdmin(final RegisterRequest registerRequest) {
    final var user = createUser(registerRequest);
    user.setRole(Role.ROLE_ADMIN);
    return userMapper.toDto(userRepository.save(user));
  }

}
