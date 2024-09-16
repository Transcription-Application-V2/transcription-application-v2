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

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    return http
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint((request, response, authException) -> {
              response.sendError(response.getStatus());
            }))

        .cors(cors -> cors
            .configurationSource(request -> corsConfiguration))

        .csrf(AbstractHttpConfigurer::disable)

        .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)

        .authorizeHttpRequests(request -> request
            .requestMatchers("/api/v2/user/create").permitAll()
            .requestMatchers("/api/v2/auth/authenticate").permitAll()
            .anyRequest().authenticated())

        .authenticationProvider(authenticationProvider)

        .sessionManagement(sessionManagement -> sessionManagement
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        .build();

  }

}
