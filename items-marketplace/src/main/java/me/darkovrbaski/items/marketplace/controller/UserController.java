package me.darkovrbaski.items.marketplace.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.dto.UserDto;
import me.darkovrbaski.items.marketplace.security.model.RegisterRequest;
import me.darkovrbaski.items.marketplace.service.intefaces.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

  UserService userService;

  @Operation(
      summary = "Get the current user.",
      description = "Returns the current user."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User found"),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  @GetMapping
  public ResponseEntity<UserDto> getCurrentUser(final Principal principal) {
    return ResponseEntity.ok(userService.getUser(principal.getName()));
  }

  @Operation(
      summary = "Get a user by username.",
      description = "Returns a user by the given username."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User found"),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{username}")
  public ResponseEntity<UserDto> getUser(@PathVariable final String username) {
    return ResponseEntity.ok(userService.getUser(username));
  }

  @Operation(
      summary = "Get all users.",
      description = "Returns a page of users."
  )
  @ApiResponse(responseCode = "200", description = "Users found")
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/all")
  public ResponseEntity<Page<UserDto>> getUsers(
      @RequestParam(name = "page", defaultValue = "0") final int page,
      @RequestParam(name = "size", defaultValue = "10") final int size
  ) {
    return ResponseEntity.ok(userService.getUsers(page, size));
  }

  @Operation(
      summary = "Search users.",
      description = "Returns a page of users by the given username."
  )
  @ApiResponse(responseCode = "200", description = "Users found")
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/search")
  public ResponseEntity<Page<UserDto>> searchUsers(
      @RequestParam(name = "username", defaultValue = "") final String username,
      @RequestParam(name = "page", defaultValue = "0") final int page,
      @RequestParam(name = "size", defaultValue = "10") final int size) {
    return ResponseEntity.ok(userService.searchUsers(username, page, size));
  }

  @Operation(
      summary = "Delete a user.",
      description = "Deletes a user by the given id."
  )
  @ApiResponse(responseCode = "204", description = "User deleted")
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable final Long userId) {
    userService.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Update a user.",
      description = "Updates a user by the given user details."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User updated"),
      @ApiResponse(responseCode = "400", description = "Invalid request"),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  @PutMapping
  public ResponseEntity<UserDto> updateUser(@Valid @RequestBody final RegisterRequest updateRequest,
      final Principal principal) {
    final var user = userService.findByUsername(principal.getName());
    if (!user.getUsername().equals(updateRequest.username())) {
      throw new IllegalArgumentException("You can't update other user");
    }
    return ResponseEntity.ok(userService.updateUser(updateRequest));
  }

  @Operation(
      summary = "Update a user image.",
      description = "Updates a user image by the given image."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User image updated"),
      @ApiResponse(responseCode = "400", description = "Invalid request"),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  @PostMapping("/image")
  public ResponseEntity<Void> updateImage(@RequestParam("image") final MultipartFile file,
      final Principal principal) {
    userService.updateImage(file, principal.getName());
    return ResponseEntity.ok().build();
  }

}