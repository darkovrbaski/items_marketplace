package me.darkovrbaski.items.marketplace.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.util.HashSet;
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

  public void addArticleItem(final ArticleItem newArticleItem) {
    if (newArticleItem == null) {
      return;
    }
    if (articleItems == null) {
      articleItems = new HashSet<>();
    }
    articleItems.add(newArticleItem);
  }

  public void removeArticleItem(final ArticleItem oldArticleItem) {
    if (oldArticleItem == null) {
      return;
    }
    if (articleItems != null) {
      articleItems.remove(oldArticleItem);
    }
  }

  public void updateArticleItem(final ArticleItem articleItem) {
    if (articleItem == null) {
      return;
    }
    if (articleItems != null) {
      articleItems.remove(articleItem);
      articleItems.add(articleItem);
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