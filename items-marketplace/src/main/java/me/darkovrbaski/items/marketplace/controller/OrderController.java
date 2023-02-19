package me.darkovrbaski.items.marketplace.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.dto.OrderDto;
import me.darkovrbaski.items.marketplace.service.intefaces.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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

  @Operation(
      summary = "Get an order by id.",
      description = "Returns details of the order "
          + "including the filled quantity and the list of trades."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Order found"),
      @ApiResponse(responseCode = "404", description = "Order not found")
  })
  @GetMapping("/{id}")
  public ResponseEntity<OrderDto> getOrder(@PathVariable final Long id) {
    return ResponseEntity.ok(orderService.getOrder(id));
  }

  @Operation(
      summary = "Get all active orders for a user.",
      description = "Returns a list of all active orders for a user."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Orders found"),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  @GetMapping("/{userId}/active")
  public ResponseEntity<List<OrderDto>> getActiveOrders(@PathVariable final Long userId) {
    return ResponseEntity.ok(orderService.getActiveOrders(userId));
  }

  @Operation(
      summary = "Get all history orders for a user.",
      description = "Returns a list of all history orders for a user."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Orders found"),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  @GetMapping("/{userId}/history")
  public ResponseEntity<Page<OrderDto>> getHistoryOrders(
      @PathVariable final Long userId,
      @RequestParam(name = "page", defaultValue = "0") final int page,
      @RequestParam(name = "size", defaultValue = "10") final int size) {
    return ResponseEntity.ok(orderService.getHistoryOrders(userId, page, size));
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
  @PostMapping
  public ResponseEntity<OrderDto> createOrder(@RequestBody final OrderDto orderDto) {
    return ResponseEntity.ok(orderService.createOrder(orderDto));
  }

}
