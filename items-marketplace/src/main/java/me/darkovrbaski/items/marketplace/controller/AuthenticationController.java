package me.darkovrbaski.items.marketplace.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.dto.UserDto;
import me.darkovrbaski.items.marketplace.security.model.AuthRequest;
import me.darkovrbaski.items.marketplace.security.model.AuthResponse;
import me.darkovrbaski.items.marketplace.security.model.RegisterRequest;
import me.darkovrbaski.items.marketplace.service.intefaces.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

  AuthenticationService authService;

  @Operation(
      summary = "Login user",
      description = "Returns a JWT token for the given user."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User logged in"),
      @ApiResponse(responseCode = "401", description = "Invalid username or password")
  })
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody final AuthRequest authRequest) {
    return ResponseEntity.ok(authService.login(authRequest));
  }

  @Operation(
      summary = "Register user",
      description = "Returns a JWT token for the given user."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User registered"),
      @ApiResponse(responseCode = "400", description = "Invalid username or password or email")
  })
  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@RequestBody final RegisterRequest registerRequest) {
    return ResponseEntity.ok(authService.register(registerRequest));
  }

  @Operation(
      summary = "Register admin",
      description = "Register new admin account."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Admin registered"),
      @ApiResponse(responseCode = "400", description = "Invalid username or password or email")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/register/admin")
  public ResponseEntity<UserDto> registerAdmin(
      @RequestBody final RegisterRequest registerRequest) {
    return ResponseEntity.ok(authService.registerAdmin(registerRequest));
  }

}
