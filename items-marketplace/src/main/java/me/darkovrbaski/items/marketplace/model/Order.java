package me.darkovrbaski.items.marketplace.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@Table(name = "`order`")
public class Order extends EntityDB {

  @Column
  LocalDateTime createdDateTime;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  OrderType type;

  @PositiveOrZero
  @Column(nullable = false)
  BigDecimal price;

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
  List<Trade> trade;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  User user;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "article_id")
  Article article;

  public List<Trade> getTrade() {
    if (trade == null) {
      trade = new ArrayList<>();
    }
    return trade;
  }

  public void setTrade(final List<Trade> newTrade) {
    removeAllTrade();
    for (final Trade value : newTrade) {
      addTrade(value);
    }
  }

  public void addTrade(final Trade newTrade) {
    if (newTrade == null) {
      return;
    }
    if (trade == null) {
      trade = new ArrayList<>();
    }
    trade.add(newTrade);
  }

  public void removeTrade(final Trade oldTrade) {
    if (oldTrade == null) {
      return;
    }
    if (trade != null) {
      trade.remove(oldTrade);
    }
  }

  public void removeAllTrade() {
    if (trade != null) {
      trade.clear();
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