package me.darkovrbaski.items.marketplace.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.dto.ArticleItemDto;
import me.darkovrbaski.items.marketplace.service.intefaces.InventoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryController {

  InventoryService inventoryService;

  @Operation(
      summary = "Get an inventory.",
      description = "Returns a current user's inventory with articles."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Inventory found"),
      @ApiResponse(responseCode = "404", description = "Inventory not found")
  })
  @GetMapping("/{userId}")
  public ResponseEntity<Page<ArticleItemDto>> getInventory(@PathVariable final Long userId,
      @RequestParam(name = "page", defaultValue = "0") final int page,
      @RequestParam(name = "size", defaultValue = "10") final int size) {
    return ResponseEntity.ok(inventoryService.getInventory(userId, page, size));
  }

  @Operation(
      summary = "Search an inventory.",
      description = "Returns a current user's inventory with articles by the given article name."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Inventory found"),
      @ApiResponse(responseCode = "404", description = "Inventory not found")
  })
  @GetMapping("/search/{userId}")
  public ResponseEntity<Page<ArticleItemDto>> searchInventory(@PathVariable final Long userId,
      @RequestParam(name = "name", defaultValue = "") final String name,
      @RequestParam(name = "page", defaultValue = "0") final int page,
      @RequestParam(name = "size", defaultValue = "10") final int size) {
    return ResponseEntity.ok(inventoryService.searchInventory(userId, name, page, size));
  }

}
