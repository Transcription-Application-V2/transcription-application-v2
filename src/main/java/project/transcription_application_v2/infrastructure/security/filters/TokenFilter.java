package project.transcription_application_v2.infrastructure.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import project.transcription_application_v2.infrastructure.security.service.UserDetailsServiceImpl;
import project.transcription_application_v2.infrastructure.security.service.AccessTokenService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(AccessTokenService.class);

  private final AccessTokenService accessTokenService;
  private final UserDetailsServiceImpl userDetailsService;

  @Override
  protected void doFilterInternal(@NotNull HttpServletRequest request,
                                  @NotNull HttpServletResponse response,
                                  @NotNull FilterChain filterChain) throws ServletException, IOException {

    String token = accessTokenService.getJWTFromRequest(request);

    try {
      if (!StringUtils.hasText(token) || !accessTokenService.validate(token) || accessTokenService.isExpired(token))
        throw new Exception("Empty, invalid or expired token.");

      String username = accessTokenService.extractUsername(token);

      UserDetails user = userDetailsService.loadUserByUsername(username);

      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

      authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

      SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    } catch (Exception exception) {
      logger.error("Cannot set user authentication: {}", exception);
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    filterChain.doFilter(request, response);
  }
}
