package me.darkovrbaski.items.marketplace.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.mapper.ArticleItemMapper;
import me.darkovrbaski.items.marketplace.model.Article;
import me.darkovrbaski.items.marketplace.model.ArticleItem;
import me.darkovrbaski.items.marketplace.model.ArticleTrade;
import me.darkovrbaski.items.marketplace.model.Inventory;
import me.darkovrbaski.items.marketplace.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@FieldDefaults(level = AccessLevel.PRIVATE)
class InventoryServiceTest {

  @Mock
  InventoryRepository inventoryRepository;

  final ArticleItemMapper articleItemMapper = Mappers.getMapper(ArticleItemMapper.class);

  EntityManager em;

  InventoryServiceImpl inventoryService;

  @BeforeEach
  void setUp() {
    inventoryService = new InventoryServiceImpl(inventoryRepository, articleItemMapper, em);
  }

  @Test
  void givenArticleTradeSumQuantity_whenUpdateItemInInventory_thenUpdatedInventory() {
    final List<Article> articles = new ArrayList<>() {
      {
        add(Article.builder().name("item1").build());
        add(Article.builder().name("item2").build());
        add(Article.builder().name("item3").build());
      }
    };
    final ArticleTrade articleTrade = new ArticleTrade(BigDecimal.valueOf(10), articles.get(0));
    final Set<ArticleItem> articleItems = new HashSet<>() {
      {
        add(new ArticleItem(BigDecimal.valueOf(5), articles.get(0)));
        add(new ArticleItem(BigDecimal.valueOf(2), articles.get(1)));
        add(new ArticleItem(BigDecimal.valueOf(1), articles.get(2)));
      }
    };
    final Inventory inventory = new Inventory(null, articleItems);
    final Set<ArticleItem> expectedArticleItems = new HashSet<>() {
      {
        add(new ArticleItem(BigDecimal.valueOf(15), articles.get(0)));
        add(new ArticleItem(BigDecimal.valueOf(2), articles.get(1)));
        add(new ArticleItem(BigDecimal.valueOf(1), articles.get(2)));
      }
    };
    final var expectedInventory = new Inventory(null, expectedArticleItems);
    when(inventoryRepository.findByIdOrThrow(any())).thenReturn(inventory);

    inventoryService.updateItemInInventory(1L, articleTrade);

    final ArgumentCaptor<Inventory> argumentCaptor = ArgumentCaptor.forClass(Inventory.class);
    verify(inventoryRepository).save(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isEqualTo(expectedInventory);
  }

  @Test
  void givenArticleTradeAddItem_whenUpdateItemInInventory_thenUpdatedInventory() {
    final List<Article> articles = new ArrayList<>() {
      {
        add(Article.builder().name("item1").build());
        add(Article.builder().name("item2").build());
        add(Article.builder().name("item3").build());
      }
    };
    final ArticleTrade articleTrade = new ArticleTrade(BigDecimal.valueOf(10), articles.get(0));
    final Set<ArticleItem> articleItems = new HashSet<>() {
      {
        add(new ArticleItem(BigDecimal.valueOf(2), articles.get(1)));
        add(new ArticleItem(BigDecimal.valueOf(1), articles.get(2)));
      }
    };
    final Inventory inventory = new Inventory(null, articleItems);
    final Set<ArticleItem> expectedArticleItems = new HashSet<>() {
      {
        add(new ArticleItem(BigDecimal.valueOf(10), articles.get(0)));
        add(new ArticleItem(BigDecimal.valueOf(2), articles.get(1)));
        add(new ArticleItem(BigDecimal.valueOf(1), articles.get(2)));
      }
    };
    final var expectedInventory = new Inventory(null, expectedArticleItems);
    when(inventoryRepository.findByIdOrThrow(any())).thenReturn(inventory);

    inventoryService.updateItemInInventory(1L, articleTrade);

    final ArgumentCaptor<Inventory> argumentCaptor = ArgumentCaptor.forClass(Inventory.class);
    verify(inventoryRepository).save(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isEqualTo(expectedInventory);
  }

  @Test
  void givenArticleTradeSubQuantity_whenUpdateItemInInventory_thenUpdatedInventory() {
    final List<Article> articles = new ArrayList<>() {
      {
        add(Article.builder().name("item1").build());
        add(Article.builder().name("item2").build());
        add(Article.builder().name("item3").build());
      }
    };
    final ArticleTrade articleTrade = new ArticleTrade(BigDecimal.valueOf(-5), articles.get(0));
    final Set<ArticleItem> articleItems = new HashSet<>() {
      {
        add(new ArticleItem(BigDecimal.valueOf(10), articles.get(0)));
        add(new ArticleItem(BigDecimal.valueOf(2), articles.get(1)));
        add(new ArticleItem(BigDecimal.valueOf(1), articles.get(2)));
      }
    };
    final Inventory inventory = new Inventory(null, articleItems);
    final Set<ArticleItem> expectedArticleItems = new HashSet<>() {
      {
        add(new ArticleItem(BigDecimal.valueOf(5), articles.get(0)));
        add(new ArticleItem(BigDecimal.valueOf(2), articles.get(1)));
        add(new ArticleItem(BigDecimal.valueOf(1), articles.get(2)));
      }
    };
    final var expectedInventory = new Inventory(null, expectedArticleItems);
    when(inventoryRepository.findByIdOrThrow(any())).thenReturn(inventory);

    inventoryService.updateItemInInventory(1L, articleTrade);

    final ArgumentCaptor<Inventory> argumentCaptor = ArgumentCaptor.forClass(Inventory.class);
    verify(inventoryRepository).save(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isEqualTo(expectedInventory);
  }

  @Test
  void givenArticleTradeRemoveItem_whenUpdateItemInInventory_thenUpdatedInventory() {
    final List<Article> articles = new ArrayList<>() {
      {
        add(Article.builder().name("item1").build());
        add(Article.builder().name("item2").build());
        add(Article.builder().name("item3").build());
      }
    };
    final ArticleTrade articleTrade = new ArticleTrade(BigDecimal.valueOf(-10), articles.get(0));
    final Set<ArticleItem> articleItems = new HashSet<>() {
      {
        add(new ArticleItem(BigDecimal.valueOf(10), articles.get(0)));
        add(new ArticleItem(BigDecimal.valueOf(2), articles.get(1)));
        add(new ArticleItem(BigDecimal.valueOf(1), articles.get(2)));
      }
    };
    final Inventory inventory = new Inventory(null, articleItems);
    final Set<ArticleItem> expectedArticleItems = new HashSet<>() {
      {
        add(new ArticleItem(BigDecimal.valueOf(2), articles.get(1)));
        add(new ArticleItem(BigDecimal.valueOf(1), articles.get(2)));
      }
    };
    final var expectedInventory = new Inventory(null, expectedArticleItems);
    when(inventoryRepository.findByIdOrThrow(any())).thenReturn(inventory);

    inventoryService.updateItemInInventory(1L, articleTrade);

    final ArgumentCaptor<Inventory> argumentCaptor = ArgumentCaptor.forClass(Inventory.class);
    verify(inventoryRepository).save(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isEqualTo(expectedInventory);
  }

  @Test
  void givenArticleTradeSubQuantity_whenUpdateItemInInventory_thenTrowNotEnough() {
    final List<Article> articles = new ArrayList<>() {
      {
        add(Article.builder().name("item1").build());
        add(Article.builder().name("item2").build());
        add(Article.builder().name("item3").build());
      }
    };
    final ArticleTrade articleTrade = new ArticleTrade(BigDecimal.valueOf(-10), articles.get(0));
    final Set<ArticleItem> articleItems = new HashSet<>() {
      {
        add(new ArticleItem(BigDecimal.valueOf(5), articles.get(0)));
        add(new ArticleItem(BigDecimal.valueOf(2), articles.get(1)));
        add(new ArticleItem(BigDecimal.valueOf(1), articles.get(2)));
      }
    };
    final Inventory inventory = new Inventory(null, articleItems);
    when(inventoryRepository.findByIdOrThrow(any())).thenReturn(inventory);

    final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      inventoryService.updateItemInInventory(1L, articleTrade);
    });

    assertEquals("Not enough items in inventory", exception.getMessage());
    verify(inventoryRepository, never()).save(any());
  }

  @Test
  void givenArticleTradeSubQuantity_whenUpdateItemInInventory_thenTrowNoItem() {
    final List<Article> articles = new ArrayList<>() {
      {
        add(Article.builder().name("item1").build());
        add(Article.builder().name("item2").build());
        add(Article.builder().name("item3").build());
      }
    };
    final ArticleTrade articleTrade = new ArticleTrade(BigDecimal.valueOf(-10), articles.get(0));
    final Set<ArticleItem> articleItems = new HashSet<>() {
      {
        add(new ArticleItem(BigDecimal.valueOf(2), articles.get(1)));
        add(new ArticleItem(BigDecimal.valueOf(1), articles.get(2)));
      }
    };
    final Inventory inventory = new Inventory(null, articleItems);
    when(inventoryRepository.findByIdOrThrow(any())).thenReturn(inventory);

    final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      inventoryService.updateItemInInventory(1L, articleTrade);
    });

    assertEquals("Not item in inventory", exception.getMessage());
    verify(inventoryRepository, never()).save(any());
  }
}
