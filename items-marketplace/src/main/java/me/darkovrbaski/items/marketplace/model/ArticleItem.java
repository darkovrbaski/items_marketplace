package me.darkovrbaski.items.marketplace.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Embeddable
@Immutable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleItem {

  @Positive
  @Column(nullable = false)
  double quantity;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "article_id")
  Article article;

}