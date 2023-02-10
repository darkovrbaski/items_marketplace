package me.darkovrbaski.items.marketplace.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.dto.MoneyDto;
import me.darkovrbaski.items.marketplace.dto.WalletDto;
import me.darkovrbaski.items.marketplace.service.intefaces.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @Operation(
      summary = "Get a wallet.",
      description = "Returns a current user's wallet details."
  )
  @ApiResponse(responseCode = "200", description = "Wallet found")
  @GetMapping("/{userId}")
  public ResponseEntity<WalletDto> getWallet(@PathVariable final Long userId) {
    return ResponseEntity.ok(walletService.getWallet(userId));
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
  @PutMapping("/{userId}/add")
  public ResponseEntity<WalletDto> addFunds(@PathVariable final Long userId,
      @Valid @RequestBody final MoneyDto amount) {
    return ResponseEntity.ok(walletService.addFunds(userId, amount));
  }

}
