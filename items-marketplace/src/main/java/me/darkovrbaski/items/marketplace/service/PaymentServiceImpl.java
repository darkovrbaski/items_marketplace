package me.darkovrbaski.items.marketplace.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.LineItem;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.Mode;
import com.stripe.param.checkout.SessionCreateParams.PaymentMethodType;
import com.stripe.param.checkout.SessionListLineItemsParams;
import com.stripe.param.checkout.SessionRetrieveParams;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.dto.MoneyDto;
import me.darkovrbaski.items.marketplace.exception.StripeGeneralException;
import me.darkovrbaski.items.marketplace.service.intefaces.PaymentService;
import me.darkovrbaski.items.marketplace.service.intefaces.UserService;
import me.darkovrbaski.items.marketplace.service.intefaces.WalletService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentServiceImpl implements PaymentService {

  final WalletService walletService;
  final UserService userService;

  @Value("${frontend.url}")
  private String frontendUrl;

  @Value("${stripe.secret-key}")
  private String apiKey;

  static final String USD = "USD";
  static final String WALLET_FUNDS = "Wallet funds";
  static final String PAYMENT_SUCCESS = "/payment/success";
  static final String PAYMENT_FAILED = "/payment/failed";
  static final String METADATA_USERNAME = "username";
  static final String EXPAND_LINE_ITEMS = "line_items";
  static final String PAYMENT_STATUS_PAID = "paid";

  @Override
  public Session createSession(final MoneyDto amount, final String username) {
    Stripe.apiKey = apiKey;
    final SessionCreateParams params = SessionCreateParams.builder()
        .addPaymentMethodType(PaymentMethodType.CARD)
        .setMode(Mode.PAYMENT)
        .setCancelUrl(frontendUrl + PAYMENT_FAILED)
        .setSuccessUrl(frontendUrl + PAYMENT_SUCCESS)
        .addLineItem(createSessionLineItem(amount))
        .putMetadata(METADATA_USERNAME, username)
        .build();

    try {
      return Session.create(params);
    } catch (final StripeException e) {
      throw new StripeGeneralException(e.getStripeError().getMessage());
    }
  }

  private SessionCreateParams.LineItem createSessionLineItem(final MoneyDto amount) {
    return SessionCreateParams.LineItem.builder()
        .setPriceData(createPriceData(amount))
        .setQuantity(1L)
        .build();
  }

  private SessionCreateParams.LineItem.PriceData createPriceData(final MoneyDto amount) {
    return SessionCreateParams.LineItem.PriceData.builder()
        .setCurrency(USD)
        .setUnitAmount(amount.amount().longValue() * 100)
        .setProductData(
            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(WALLET_FUNDS)
                .build())
        .build();
  }

  @Override
  public void handleSessionCompletedEvent(final Event event) {
    final Session session = retrieveSession(event);
    if (session.getPaymentStatus().equals(PAYMENT_STATUS_PAID)) {
      final LineItem lineItem = extractLineItem(session);
      final String username = session.getMetadata().get(METADATA_USERNAME);
      fulfillOrder(lineItem, username);
    }
  }

  private Session retrieveSession(final Event event) {
    final Session session = (Session) event.getDataObjectDeserializer().getObject().orElseThrow();
    final SessionRetrieveParams params = SessionRetrieveParams.builder()
        .addExpand(EXPAND_LINE_ITEMS)
        .build();

    try {
      return Session.retrieve(session.getId(), params, null);
    } catch (final StripeException e) {
      throw new StripeGeneralException(e.getStripeError().getMessage());
    }
  }

  private LineItem extractLineItem(final Session session) {
    final SessionListLineItemsParams listLineItemsParams =
        SessionListLineItemsParams.builder().build();

    try {
      return session.listLineItems(listLineItemsParams).getData().get(0);
    } catch (final StripeException e) {
      throw new StripeGeneralException(e.getStripeError().getMessage());
    }
  }

  private void fulfillOrder(final LineItem lineItem, final String username) {
    final MoneyDto amount = new MoneyDto(BigDecimal.valueOf(lineItem.getAmountTotal() / 100), USD);
    final var user = userService.findByUsername(username);
    walletService.addFunds(user.getId(), amount);
  }

}
