package me.darkovrbaski.items.marketplace.service.intefaces;

import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import me.darkovrbaski.items.marketplace.dto.MoneyDto;

public interface PaymentService {

  Session createSession(MoneyDto amount, String username);

  void handleSessionCompletedEvent(Event event);

}
