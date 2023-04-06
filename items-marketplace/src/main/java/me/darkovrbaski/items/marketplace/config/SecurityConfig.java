package me.darkovrbaski.items.marketplace.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.darkovrbaski.items.marketplace.security.DelegateAccessDeniedHandler;
import me.darkovrbaski.items.marketplace.security.DelegateAuthenticationEntryPoint;
import me.darkovrbaski.items.marketplace.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SecurityConfig {

  JwtAuthenticationFilter jwtAuthFilter;
  AuthenticationProvider authProvider;
  DelegateAuthenticationEntryPoint authEntryPoint;
  DelegateAccessDeniedHandler accessDeniedHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .cors()
        .and()
        .authorizeHttpRequests()
        .requestMatchers(HttpMethod.GET, "/orderBook/{articleId}").permitAll()

        .requestMatchers(HttpMethod.GET, "/article").permitAll()
        .requestMatchers(HttpMethod.GET, "/article/search").permitAll()
        .requestMatchers(HttpMethod.GET, "/article/find/{name}").permitAll()
        .requestMatchers(HttpMethod.GET, "/article/{id}").permitAll()

        .requestMatchers(HttpMethod.POST, "/wallet/success").permitAll()

        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

        .requestMatchers(HttpMethod.GET, "/docs/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(authEntryPoint)
        .accessDeniedHandler(accessDeniedHandler)
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authenticationProvider(authProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

}
