package me.darkovrbaski.items.marketplace.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@Table(name = "trade")
public class Trade extends EntityDb {

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  OrderType type;

  @Column
  LocalDateTime createdDateTime;

  @PositiveOrZero
  @Column(nullable = false)
  BigDecimal price;

  @Positive
  @Column(nullable = false)
  BigDecimal quantity;

  @Column(nullable = false)
  long sellOrderId;

  @Column(nullable = false)
  long buyOrderId;

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    final Trade trade = (Trade) o;
    return Objects.equals(getId(), trade.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}