package project.transcription_application_v2.infrastructure.security.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import project.transcription_application_v2.domain.user.entity.User;
import project.transcription_application_v2.infrastructure.security.entity.UserDetailsImpl;

@Component(value = "userPermissionEvaluator")
@RequiredArgsConstructor
public class UserPermissionEvaluator {

  public boolean ownerUserAccess(Authentication authentication, Long id) {
    if (authentication != null
        && authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {

      User currentUser = userDetails.getUser();

      return currentUser.getId().equals(id);
    }
    return false;
  }

}
