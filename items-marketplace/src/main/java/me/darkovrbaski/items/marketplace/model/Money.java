package me.darkovrbaski.items.marketplace.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Immutable;
import org.hibernate.validator.constraints.Length;

@Embeddable
@Immutable
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Money {

  static Currency USD = Currency.getInstance("USD");
  static RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;

  @PositiveOrZero(message = "Amount must be positive or zero")
  @Column(nullable = false)
  BigDecimal amount;

  @Length(min = 3, max = 3, message = "Currency must be 3 characters long")
  @Pattern(regexp = "USD", message = "Currency must be USD")
  @Column(nullable = false, length = 3)
  Currency currency;

  protected Money() {
    this(BigDecimal.ZERO, USD);
  }

  public Money(final BigDecimal amount, final Currency currency) {
    this(amount, currency, DEFAULT_ROUNDING);
  }

  Money(final BigDecimal amount, final Currency currency, final RoundingMode rounding) {
    this.currency = currency;
    this.amount = amount.setScale(currency.getDefaultFractionDigits(), rounding);
  }

  public static Money dollars(final BigDecimal amount) {
    return new Money(amount, USD);
  }

  public Money add(final Money money) {
    if (!getCurrency().equals(money.getCurrency())) {
      throw new IllegalArgumentException("Currencies must be the same");
    }
    return new Money(getAmount().add(money.getAmount()), getCurrency());
  }

  public Money subtract(final Money money) {
    if (!getCurrency().equals(money.getCurrency())) {
      throw new IllegalArgumentException("Currencies must be the same");
    }
    return new Money(getAmount().subtract(money.getAmount()), getCurrency());
  }

  public String getCurrencyCode() {
    return getCurrency().getCurrencyCode();
  }

  @Override
  public String toString() {
    return getCurrency().getSymbol() + " " + getAmount();
  }

  public String toString(final Locale locale) {
    return getCurrency().getSymbol(locale) + " " + getAmount();
  }

}
