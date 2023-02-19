package me.darkovrbaski.items.marketplace.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "`order`")
public class Order extends EntityDb {

  @Column
  LocalDateTime createdDateTime;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  OrderType type;

  @Embedded
  @Column(nullable = false)
  Money price;

  @Positive
  @Column(nullable = false)
  BigDecimal quantity;

  @PositiveOrZero
  @Column(nullable = false)
  BigDecimal filledQuantity;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  OrderStatus status;

  @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
  @JoinTable(
      name = "order_trades",
      joinColumns = {@JoinColumn(name = "order_id")},
      inverseJoinColumns = {@JoinColumn(name = "trade_id")}
  )
  List<Trade> trades;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  User user;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "article_id")
  Article article;

  public BigDecimal getRemainingQuantity() {
    return quantity.subtract(filledQuantity);
  }

  public void setClosedAndFulfilled() {
    status = OrderStatus.CLOSED;
    filledQuantity = quantity;
  }

  public boolean isBuyOrder() {
    return type == OrderType.BUY;
  }

  public boolean isClosed() {
    return status == OrderStatus.CLOSED;
  }

  public void addFilledQuantity(final BigDecimal newFilledQuantity) {
    if (newFilledQuantity.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Filled quantity cannot be negative");
    }
    if (filledQuantity.add(newFilledQuantity).compareTo(quantity) > 0) {
      throw new IllegalArgumentException("Filled quantity cannot be greater than quantity");
    }
    filledQuantity = filledQuantity.add(newFilledQuantity);
    if (filledQuantity.compareTo(quantity) == 0) {
      status = OrderStatus.CLOSED;
    }
  }

  public void setTrades(final List<Trade> newTrade) {
    removeAllTrade();
    for (final Trade value : newTrade) {
      addTrade(value);
    }
  }

  public void addTrade(final Trade newTrade) {
    if (newTrade == null) {
      return;
    }
    if (trades == null) {
      trades = new ArrayList<>();
    }
    trades.add(newTrade);
  }

  public void removeAllTrade() {
    if (trades != null) {
      trades.clear();
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
    final Order order = (Order) o;
    return Objects.equals(getId(), order.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

}