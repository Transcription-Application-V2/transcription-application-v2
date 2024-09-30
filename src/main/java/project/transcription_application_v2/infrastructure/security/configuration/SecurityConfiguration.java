package project.transcription_application_v2.infrastructure.security.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import project.transcription_application_v2.infrastructure.security.filters.TokenFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final TokenFilter tokenFilter;
  private final CorsConfiguration corsConfiguration;
  private final AuthenticationProvider authenticationProvider;
  private static final String[] WHITE_LIST = {
      "/v2/api-docs",
      "/v3/api-docs",
      "/v3/api-docs/**",
      "/swagger-resources",
      "/swagger-resources/**",
      "/configuration/ui",
      "/configuration/security",
      "/swagger-ui/**",
      "/webjars/**",
      "/swagger-ui.html",
      "/api/v2/user/create",
      "/api/v2/auth/authenticate",
      "/api/v2/auth/refresh-token",
  };

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    return http
        .cors(cors -> cors
            .configurationSource(request -> corsConfiguration))

        .csrf(AbstractHttpConfigurer::disable)

        .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)

        .authorizeHttpRequests(request -> request
            .requestMatchers(WHITE_LIST).permitAll()
            .anyRequest().authenticated())

        .authenticationProvider(authenticationProvider)

        .sessionManagement(sessionManagement -> sessionManagement
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        .build();

  }

}
