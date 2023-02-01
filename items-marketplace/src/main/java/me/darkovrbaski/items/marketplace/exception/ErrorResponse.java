package me.darkovrbaski.items.marketplace.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
public class ErrorResponse {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  LocalDateTime timestamp;

  int code;

  String status;

  String message;

  List<String> errors;

  public ErrorResponse(final HttpStatus httpStatus, final String message) {
    timestamp = LocalDateTime.now();
    code = httpStatus.value();
    status = httpStatus.name();
    this.message = message;
    errors = null;
  }

  public ErrorResponse(final HttpStatus httpStatus, final String message,
      final List<String> errors) {
    timestamp = LocalDateTime.now();
    code = httpStatus.value();
    status = httpStatus.name();
    this.message = message;
    this.errors = errors;
  }

}