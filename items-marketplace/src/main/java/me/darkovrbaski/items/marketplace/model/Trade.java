package me.darkovrbaski.items.marketplace.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "trade")
public class Trade extends EntityDb {

  @Column
  LocalDateTime createdDateTime;

  @Embedded
  @Column(nullable = false)
  Money price;

  @Positive
  @Column(nullable = false)
  BigDecimal quantity;

  @Column(nullable = false)
  Long sellOrderId;

  @Column(nullable = false)
  Long buyOrderId;

  public BigDecimal getTotal() {
    return price.getAmount().multiply(quantity);
  }

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