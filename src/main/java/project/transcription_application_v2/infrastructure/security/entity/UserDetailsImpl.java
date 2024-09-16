package project.transcription_application_v2.infrastructure.security.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.transcription_application_v2.domain.user.entity.User;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Getter
public class UserDetailsImpl implements UserDetails {

  private final User user;

  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
  }

  public String getPassword() {
    return user.getPassword();
  }

  public String getUsername() {
    return user.getUsername();
  }

  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }

  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }

  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
  }

}
