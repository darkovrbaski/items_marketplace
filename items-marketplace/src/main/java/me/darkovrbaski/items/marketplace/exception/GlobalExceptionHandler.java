package me.darkovrbaski.items.marketplace.exception;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends BaseExceptionHandler {

  GlobalExceptionHandler() {
    super(log);

    registerMapping(EntityNotFoundException.class, HttpStatus.NOT_FOUND);
    registerMapping(EntityAlreadyExistsException.class, HttpStatus.CONFLICT);
    registerMapping(MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST);
    registerMapping(EntityIntegrityViolationException.class, HttpStatus.CONFLICT);
    registerMapping(AuthenticationException.class, HttpStatus.UNAUTHORIZED);
    registerMapping(InsufficientAuthenticationException.class, HttpStatus.UNAUTHORIZED);
    registerMapping(BadCredentialsException.class, HttpStatus.UNAUTHORIZED);
    registerMapping(AccessDeniedException.class, HttpStatus.FORBIDDEN);
    registerMapping(UsernameNotFoundException.class, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(
      final MethodArgumentNotValidException ex) {
    final HttpStatus status = getStatusFromException(ex);
    final List<String> errors = ex.getBindingResult().getFieldErrors().stream()
        .map(FieldError::getDefaultMessage).toList();
    final String message = "Validation failed";

    if (log.isErrorEnabled()) {
      log.error("{} : {} ({}) \n\n {}", message, status.name(), status.value(), ex.getMessage(),
          ex);
    }

    return ResponseEntity.status(status).body(new ErrorResponse(status, message, errors));
  }

}