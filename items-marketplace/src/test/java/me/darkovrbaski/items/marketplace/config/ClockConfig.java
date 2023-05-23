package me.darkovrbaski.items.marketplace.config;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ClockConfig {

  static final ZonedDateTime NOW = ZonedDateTime.of(
      LocalDateTime.of(2000, 1, 1, 0, 0, 0),
      ZoneId.of("UTC")
  );

  @Bean
  public Clock clock() {
    return Clock.fixed(NOW.toInstant(), NOW.getZone());
  }

}