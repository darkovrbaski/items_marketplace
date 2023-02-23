package me.darkovrbaski.items.marketplace.service.intefaces;

import me.darkovrbaski.items.marketplace.dto.UserDto;
import me.darkovrbaski.items.marketplace.security.model.AuthRequest;
import me.darkovrbaski.items.marketplace.security.model.AuthResponse;
import me.darkovrbaski.items.marketplace.security.model.RegisterRequest;

public interface AuthenticationService {

  AuthResponse login(AuthRequest authRequest);

  AuthResponse register(RegisterRequest registerRequest);

  UserDto registerAdmin(RegisterRequest registerRequest);
  
}
