package me.darkovrbaski.items.marketplace.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SwaggerConfig {

  static String SECURITY_SCHEME_NAME = "BearerTokenAuth";
  static String SCHEME_BEARER = "bearer";
  static String BEARER_FORMAT_JWT = "JWT";


  @Bean
  public OpenAPI customizeOpenApi() {
    return new OpenAPI()
        .addSecurityItem(new SecurityRequirement()
            .addList(SECURITY_SCHEME_NAME))
        .components(new Components()
            .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme(SCHEME_BEARER)
                .bearerFormat(BEARER_FORMAT_JWT)));
  }

}