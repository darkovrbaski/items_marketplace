package me.darkovrbaski.items.marketplace.exception;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler extends BaseExceptionHandler {

  CustomExceptionHandler() {
    super(log);

    registerMapping(EntityNotFoundException.class, HttpStatus.NOT_FOUND);
    registerMapping(EntityAlreadyExistsException.class, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(
      final MethodArgumentNotValidException ex) {
    final HttpStatus status = HttpStatus.BAD_REQUEST;
    final List<String> errors = ex.getBindingResult().getFieldErrors().stream()
        .map(FieldError::getDefaultMessage).collect(Collectors.toList());
    final String message = "Validation failed";

    log.error("{} : {} ({}) \n\n {}", message, status.name(), status.value(), ex.getMessage(), ex);

    return ResponseEntity.status(status).body(new ErrorResponse(status, message, errors));
  }

}