package me.darkovrbaski.items.marketplace.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public abstract class BaseExceptionHandler {

  static HttpStatus DEFAULT_ERROR = INTERNAL_SERVER_ERROR;
  Map<Class<?>, HttpStatus> exceptionMappings = new HashMap<>();
  Logger log;

  protected BaseExceptionHandler(final Logger log) {
    this.log = log;

    registerMapping(MissingServletRequestParameterException.class, BAD_REQUEST);
    registerMapping(MethodArgumentTypeMismatchException.class, BAD_REQUEST);
    registerMapping(HttpRequestMethodNotSupportedException.class, METHOD_NOT_ALLOWED);
    registerMapping(ServletRequestBindingException.class, BAD_REQUEST);
    registerMapping(InvalidParameterException.class, HttpStatus.BAD_REQUEST);
  }

  protected void registerMapping(final Class<?> clazz, final HttpStatus status) {
    exceptionMappings.put(clazz, status);
  }

  @ExceptionHandler(Throwable.class)
  @ResponseBody
  private ResponseEntity<ErrorResponse> handleThrowable(final Throwable ex) {
    final HttpStatus status = getStatusFromException(ex);

    if (log.isErrorEnabled()) {
      log.error("{} ({}) \n\n {}", status.name(), status.value(), ex.getMessage(), ex);
    }

    return ResponseEntity.status(status).body(new ErrorResponse(status, ex.getMessage()));
  }

  protected HttpStatus getStatusFromException(final Throwable ex) {
    return exceptionMappings.getOrDefault(ex.getClass(), DEFAULT_ERROR);
  }

}