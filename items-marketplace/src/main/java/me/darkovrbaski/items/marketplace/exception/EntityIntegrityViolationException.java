package me.darkovrbaski.items.marketplace.exception;

public class EntityIntegrityViolationException extends RuntimeException {

  public EntityIntegrityViolationException(final String message) {
    super(message);
  }
}
