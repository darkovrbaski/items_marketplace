package me.darkovrbaski.items.marketplace.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebConfig implements WebMvcConfigurer {

  static final String ALL_PATHS = "/**";
  static final String[] ALLOWED_METHODS = {"HEAD", "GET", "POST", "PUT", "DELETE", "PATCH",
      "OPTIONS"};

  @Value("${frontend.url}")
  String frontendUrl;

  @Override
  public void addCorsMappings(final CorsRegistry registry) {
    registry.addMapping(ALL_PATHS)
        .allowedOrigins(frontendUrl)
        .allowedMethods(ALLOWED_METHODS)
        .allowCredentials(true);
  }

}
