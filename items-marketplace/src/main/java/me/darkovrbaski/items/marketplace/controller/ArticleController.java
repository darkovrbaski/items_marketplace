package me.darkovrbaski.items.marketplace.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.dto.ArticleDto;
import me.darkovrbaski.items.marketplace.service.intefaces.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArticleController {

  ArticleService articleService;

  @Operation(
      summary = "Get all articles.",
      description = "Returns a list of all articles with the given page number and size."
  )
  @ApiResponse(responseCode = "200", description = "Articles found")
  @GetMapping
  public ResponseEntity<Page<ArticleDto>> getArticles(
      @RequestParam(name = "page", defaultValue = "0") final int page,
      @RequestParam(name = "size", defaultValue = "10") final int size) {
    return ResponseEntity.ok(articleService.getArticles(page, size));
  }

  @Operation(
      summary = "Get an article by id.",
      description = "Returns details of an article with the given id."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Article found"),
      @ApiResponse(responseCode = "404", description = "Article not found")
  })
  @GetMapping("/{id}")
  public ResponseEntity<ArticleDto> getArticle(@PathVariable final Long id) {
    return ResponseEntity.ok(articleService.getArticle(id));
  }

  @Operation(
      summary = "Get an article by name.",
      description = "Returns details of an article with the given name."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Article found"),
      @ApiResponse(responseCode = "404", description = "Article not found")
  })
  @GetMapping("/find/{name}")
  public ResponseEntity<ArticleDto> getArticle(@PathVariable final String name) {
    return ResponseEntity.ok(articleService.getArticle(name));
  }

  @Operation(
      summary = "Search articles.",
      description = "Returns a list of articles that match the given name."
  )
  @ApiResponse(responseCode = "200", description = "Articles found")
  @GetMapping("/search")
  public ResponseEntity<Page<ArticleDto>> searchArticles(
      @RequestParam(name = "name", defaultValue = "") final String name,
      @RequestParam(name = "page", defaultValue = "0") final int page,
      @RequestParam(name = "size", defaultValue = "10") final int size) {
    return ResponseEntity.ok(articleService.searchArticles(name, page, size));
  }

  @Operation(
      summary = "Create a new article.",
      description = "Creates a new article with the given details."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Article created"),
      @ApiResponse(responseCode = "400", description = "Invalid input"),
      @ApiResponse(responseCode = "409", description = "Article already exists")
  })
  @PostMapping
  public ResponseEntity<ArticleDto> createArticle(@Valid @RequestBody final ArticleDto article) {
    return ResponseEntity.status(HttpStatus.CREATED).body(articleService.createArticle(article));
  }

  @Operation(
      summary = "Update an article.",
      description = "Updates an article with the given details."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Article updated"),
      @ApiResponse(responseCode = "409", description = "Invalid input"),
      @ApiResponse(responseCode = "404", description = "Article not found")
  })
  @PutMapping
  public ResponseEntity<ArticleDto> updateArticle(@Valid @RequestBody final ArticleDto article) {
    return ResponseEntity.ok(articleService.updateArticle(article));
  }

  @Operation(
      summary = "Delete an article.",
      description = "Deletes an article with the given id."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Article deleted"),
      @ApiResponse(responseCode = "409", description = "Article is in use"),
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteArticle(@PathVariable final Long id) {
    articleService.deleteArticle(id);
    return ResponseEntity.noContent().build();
  }

}
