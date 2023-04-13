package me.darkovrbaski.items.marketplace.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.dto.OrderDto;
import me.darkovrbaski.items.marketplace.service.intefaces.OrderService;
import me.darkovrbaski.items.marketplace.service.intefaces.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

  OrderService orderService;
  UserService userService;

  @Operation(
      summary = "Get an order by id.",
      description = "Returns details of the order "
          + "including the filled quantity and the list of trades."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Order found"),
      @ApiResponse(responseCode = "404", description = "Order not found")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{id}")
  public ResponseEntity<OrderDto> getOrder(@PathVariable final Long id) {
    return ResponseEntity.ok(orderService.getOrder(id));
  }

  @Operation(
      summary = "Get an user order by id.",
      description = "Returns details of the order "
          + "including the filled quantity and the list of trades."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Order found"),
      @ApiResponse(responseCode = "404", description = "Order not found")
  })
  @GetMapping("/user/{id}")
  public ResponseEntity<OrderDto> getUserOrder(@PathVariable final Long id,
      final Principal principal) {
    final var user = userService.findByUsername(principal.getName());
    return ResponseEntity.ok(orderService.getUserOrder(id, user.getUsername()));
  }

  @Operation(
      summary = "Get all active orders for a user.",
      description = "Returns a list of all active orders for a user."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Orders found"),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/active")
  public ResponseEntity<List<OrderDto>> getActiveOrders(final Principal principal) {
    final var user = userService.findByUsername(principal.getName());
    return ResponseEntity.ok(orderService.getActiveOrders(user.getId()));
  }

  @Operation(
      summary = "Get all history orders for a user.",
      description = "Returns a list of all history orders for a user."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Orders found"),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/history")
  public ResponseEntity<Page<OrderDto>> getHistoryOrders(
      @RequestParam(name = "page", defaultValue = "0") final int page,
      @RequestParam(name = "size", defaultValue = "10") final int size,
      final Principal principal
  ) {
    final var user = userService.findByUsername(principal.getName());
    return ResponseEntity.ok(orderService.getHistoryOrders(user.getId(), page, size));
  }

  @Operation(
      summary = "Create a buy or sell order. ",
      description = "If the order is a sell order, it will be matched against existing buy orders. "
          + "If the order is a buy order, it will be matched against existing sell orders. "
          + "For each match, a trade will be created. "
          + "If the order quantity is greater than the matched order quantity, "
          + "the order will be partially filled. "
          + "If the order quantity is less than the matched order quantity, "
          + "the matched order will be partially filled. "
          + "If the order quantity is equal to the matched order quantity, "
          + "the matched order will be fully filled. "
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Order created"),
      @ApiResponse(responseCode = "400", description = "Invalid order")
  })
  @PreAuthorize("hasRole('USER')")
  @PostMapping
  public ResponseEntity<OrderDto> createOrder(
      @RequestBody final OrderDto orderDto,
      final Principal principal
  ) {
    isOrderOwnedByUser(orderDto, principal);
    return ResponseEntity.ok(orderService.createOrder(orderDto));
  }

  @Operation(
      summary = "Delete an order.",
      description = "Deletes an order with the given id."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Order deleted"),
      @ApiResponse(responseCode = "404", description = "Order not found")
  })
  @PreAuthorize("hasRole('USER')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOrder(@PathVariable final Long id) {
    orderService.closeOrder(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Get all matched sell orders for a buy order.",
      description = "Returns a list of all matched sell orders for a buy order."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Orders found"),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  @PreAuthorize("hasRole('USER')")
  @PostMapping("/matched")
  public ResponseEntity<Page<OrderDto>> getMatchedSellOrders(
      @RequestBody @Valid final OrderDto orderDto,
      @RequestParam(name = "page", defaultValue = "0") final int page,
      @RequestParam(name = "size", defaultValue = "10") final int size,
      final Principal principal
  ) {
    isOrderOwnedByUser(orderDto, principal);
    return ResponseEntity.ok(orderService.getMatchedSellOrders(orderDto, page, size));
  }

  @Operation(
      summary = "Trade an order.",
      description = "Trades an order with the given order id."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Order traded"),
      @ApiResponse(responseCode = "404", description = "Order not found")
  })
  @PreAuthorize("hasRole('USER')")
  @PostMapping("/trade/{matchedOrderId}")
  public ResponseEntity<OrderDto> tradeOrder(
      @RequestBody final OrderDto orderDto,
      @PathVariable(name = "matchedOrderId") final Long matchedOrderId,
      final Principal principal
  ) {
    isOrderOwnedByUser(orderDto, principal);
    return ResponseEntity.ok(orderService.trade(orderDto, matchedOrderId));
  }

  private void isOrderOwnedByUser(final OrderDto orderDto, final Principal principal) {
    final var user = userService.findByUsername(principal.getName());

    if (!orderDto.user().id().equals(user.getId())) {
      throw new IllegalArgumentException("Order does not belong to user");
    }
  }

}
