package me.darkovrbaski.items.marketplace.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "inventory")
public class Inventory extends EntityDb {

  @MapsId
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  User user;

  @ElementCollection(fetch = FetchType.EAGER)
  Set<ArticleItem> articleItems;

  public void updateArticleItem(final ArticleTrade articleTrade) {
    if (articleTrade == null) {
      return;
    }
    if (articleItems == null) {
      return;
    }
    if (articleTrade.quantity().compareTo(BigDecimal.ZERO) == 0) {
      throw new IllegalArgumentException("Quantity cannot be zero");
    }
    if (articleTrade.quantity().compareTo(BigDecimal.ZERO) > 0) {
      addOrSumArticleItem(articleTrade);
    } else {
      subtractOrRemoveArticleItem(articleTrade);
    }
  }

  private void subtractOrRemoveArticleItem(final ArticleTrade articleTrade) {
    final var articleItem = articleItems.stream()
        .filter(item -> item.article.getName().equals(articleTrade.article().getName()))
        .findFirst()
        .orElse(null);
    if (articleItem != null) {
      if (articleItem.quantity.add(articleTrade.quantity()).compareTo(BigDecimal.ZERO) < 0) {
        throw new IllegalArgumentException("Not enough items in inventory");
      }
      articleItem.quantity = articleItem.quantity.add(articleTrade.quantity());
      if (articleItem.quantity.compareTo(BigDecimal.ZERO) == 0) {
        articleItems.remove(articleItem);
      }
    } else {
      throw new IllegalArgumentException("Not item in inventory");
    }
  }

  private void addOrSumArticleItem(final ArticleTrade articleTrade) {
    final var articleItem = articleItems.stream()
        .filter(item -> item.article.getName().equals(articleTrade.article().getName()))
        .findFirst()
        .orElse(null);
    if (articleItem != null) {
      articleItem.quantity = articleItem.quantity.add(articleTrade.quantity());
    } else {
      articleItems.add(new ArticleItem(articleTrade.quantity(), articleTrade.article()));
    }
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    final Inventory inventory = (Inventory) o;
    return Objects.equals(getId(), inventory.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

}