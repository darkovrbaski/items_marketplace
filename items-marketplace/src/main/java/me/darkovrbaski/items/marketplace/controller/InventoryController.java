package me.darkovrbaski.items.marketplace.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.security.Principal;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.dto.ArticleItemDto;
import me.darkovrbaski.items.marketplace.service.intefaces.InventoryService;
import me.darkovrbaski.items.marketplace.service.intefaces.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryController {

  InventoryService inventoryService;
  UserService userService;

  @Operation(
      summary = "Get an inventory.",
      description = "Returns a current user's inventory with articles."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Inventory found"),
      @ApiResponse(responseCode = "404", description = "Inventory not found")
  })
  @PreAuthorize("hasRole('USER')")
  @GetMapping
  public ResponseEntity<Page<ArticleItemDto>> getInventory(
      @RequestParam(name = "page", defaultValue = "0") final int page,
      @RequestParam(name = "size", defaultValue = "10") final int size,
      final Principal principal
  ) {
    final var user = userService.findByUsername(principal.getName());
    return ResponseEntity.ok(inventoryService.getInventory(user.getId(), page, size));
  }

  @Operation(
      summary = "Search an inventory.",
      description = "Returns a current user's inventory with articles by the given article name."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Inventory found"),
      @ApiResponse(responseCode = "404", description = "Inventory not found")
  })
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/search")
  public ResponseEntity<Page<ArticleItemDto>> searchInventory(
      @RequestParam(name = "name", defaultValue = "") final String name,
      @RequestParam(name = "page", defaultValue = "0") final int page,
      @RequestParam(name = "size", defaultValue = "10") final int size,
      final Principal principal
  ) {
    final var user = userService.findByUsername(principal.getName());
    return ResponseEntity.ok(inventoryService.searchInventory(user.getId(), name, page, size));
  }

}
