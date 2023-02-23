package me.darkovrbaski.items.marketplace.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.dto.MoneyDto;
import me.darkovrbaski.items.marketplace.dto.WalletDto;
import me.darkovrbaski.items.marketplace.service.intefaces.UserService;
import me.darkovrbaski.items.marketplace.service.intefaces.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WalletController {

  WalletService walletService;
  UserService userService;

  @Operation(
      summary = "Get a wallet.",
      description = "Returns a current user's wallet details."
  )
  @ApiResponse(responseCode = "200", description = "Wallet found")
  @PreAuthorize("hasRole('USER')")
  @GetMapping
  public ResponseEntity<WalletDto> getWallet(final Principal principal) {
    final var user = userService.findByUsername(principal.getName());
    return ResponseEntity.ok(walletService.getWallet(user.getId()));
  }

  @Operation(
      summary = "Add funds to a wallet.",
      description = "Adds funds to the current user's wallet by the given amount."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Funds added"),
      @ApiResponse(responseCode = "404", description = "User not found"),
      @ApiResponse(responseCode = "400", description = "Invalid amount")
  })
  @PreAuthorize("hasRole('USER')")
  @PutMapping("/add")
  public ResponseEntity<WalletDto> addFunds(
      @Valid @RequestBody final MoneyDto amount,
      final Principal principal
  ) {
    final var user = userService.findByUsername(principal.getName());
    return ResponseEntity.ok(walletService.addFunds(user.getId(), amount));
  }

}
