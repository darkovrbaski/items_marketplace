package me.darkovrbaski.items.marketplace.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;
import lombok.AccessLevel;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "article")
public class Article extends EntityDb {

  @NotBlank(message = "Name is mandatory")
  @Column(nullable = false, unique = true)
  String name;

  @Column
  String description;

  @Column
  String image;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  QuantityType quantityType;

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    final Article article = (Article) o;
    return Objects.equals(getId(), article.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}