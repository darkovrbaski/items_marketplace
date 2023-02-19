package me.darkovrbaski.items.marketplace.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.model.OrderBook;
import me.darkovrbaski.items.marketplace.service.intefaces.OrderBookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orderBook")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderBookController {

  OrderBookService orderBookService;

  @Operation(
      summary = "Get an order book by article id.",
      description = "Returns details of the order book "
          + "including the list of open sell orders and the list of open buy orders."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Order book found"),
      @ApiResponse(responseCode = "404", description = "Article not found")
  })
  @GetMapping("/{articleId}")
  public ResponseEntity<OrderBook> getOrderBook(@PathVariable final Long articleId) {
    return ResponseEntity.ok(orderBookService.getOrderBook(articleId));
  }

}
