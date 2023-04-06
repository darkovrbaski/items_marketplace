package me.darkovrbaski.items.marketplace.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
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
import me.darkovrbaski.items.marketplace.model.StripeResponse;
import me.darkovrbaski.items.marketplace.service.intefaces.PaymentService;
import me.darkovrbaski.items.marketplace.service.intefaces.UserService;
import me.darkovrbaski.items.marketplace.service.intefaces.WalletService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WalletController {

  final WalletService walletService;
  final UserService userService;
  final PaymentService paymentService;

  @Value("${stripe.endpoint-secret}")
  private String endpointSecret;
  static final String CHECKOUT_SESSION_COMPLETED = "checkout.session.completed";

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
      @ApiResponse(responseCode = "200", description = "Stripe session created"),
      @ApiResponse(responseCode = "404", description = "User not found"),
      @ApiResponse(responseCode = "400", description = "Invalid amount")
  })
  @PreAuthorize("hasRole('USER')")
  @PostMapping("/add")
  public ResponseEntity<StripeResponse> addFunds(@Valid @RequestBody final MoneyDto amount,
      final Principal principal) {
    final Session session = paymentService.createSession(amount, principal.getName());
    return ResponseEntity.ok(new StripeResponse(session.getId()));
  }

  @Operation(
      summary = "Webhook for adding funds to a wallet.",
      description = "Handles the webhook for adding funds to the user's wallet."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Webhook handled"),
      @ApiResponse(responseCode = "400", description = "Invalid signature")
  })
  @PostMapping("/add/success")
  public ResponseEntity<?> successWebhook(@RequestBody final String payload,
      @RequestHeader("Stripe-Signature") final String sigHeader) {
    final Event event;

    try {
      event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
    } catch (final SignatureVerificationException e) {
      return ResponseEntity.badRequest().build();
    }

    if (event.getType().equals(CHECKOUT_SESSION_COMPLETED)) {
      paymentService.handleSessionCompletedEvent(event);
    }

    return ResponseEntity.ok().build();
  }

}