package com.github.jimtrung.theater.config;

import com.github.jimtrung.theater.filter.JwtAuthFilter;
import com.github.jimtrung.theater.util.AuthTokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final AuthTokenUtil authTokenUtil;

  public SecurityConfig(AuthTokenUtil authTokenUtil) {
    this.authTokenUtil = authTokenUtil;
  }

  @Bean
  public JwtAuthFilter jwtAuthFilter() {
    return new JwtAuthFilter(authTokenUtil);
  }

  /**
   * Chain #1 — Public API endpoints (auth)
   * These should NEVER trigger OAuth redirect
   */
  @Bean
  @Order(1)
  public SecurityFilterChain authChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/auth/**") // Only match /auth/**
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable);

    return http.build();
  }

  /**
   * Chain #2 — Protected endpoints + OAuth
   */
  @Bean
  @Order(2)
  public SecurityFilterChain mainChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/oauth/**",
                "/oauth2/authorization/**",
                "/login/oauth2/**",
                "error"
            ).permitAll()
            .anyRequest().authenticated()
        )
        .oauth2Login(oauth2 -> oauth2
            .loginPage("/oauth2/authorization/github")
            .defaultSuccessUrl("/oauth/login", true)
        )
        .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
