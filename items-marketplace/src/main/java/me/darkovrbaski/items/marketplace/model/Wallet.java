package me.darkovrbaski.items.marketplace.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "wallet")
public class Wallet extends EntityDb {

  @Embedded
  @Column(nullable = false)
  Money balance;

  @MapsId
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  User user;

  public void addFunds(final Money amount) {
    balance = balance.add(amount);
  }

  public void spendFunds(final Money amount) {
    if (balance.getAmount().compareTo(amount.getAmount()) < 0) {
      throw new IllegalArgumentException("Not enough funds");
    }
    balance = balance.subtract(amount);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    final Wallet wallet = (Wallet) o;
    return getId() != null && Objects.equals(getId(), wallet.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

}
