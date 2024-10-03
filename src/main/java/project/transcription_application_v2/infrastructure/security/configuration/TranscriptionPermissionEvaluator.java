package project.transcription_application_v2.infrastructure.security.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import project.transcription_application_v2.domain.transcription.service.TranscriptionService;
import project.transcription_application_v2.domain.user.entity.User;
import project.transcription_application_v2.infrastructure.exceptions.throwable.NotFoundException;
import project.transcription_application_v2.infrastructure.security.entity.UserDetailsImpl;

@Component(value = "transcriptionPermissionEvaluator")
@RequiredArgsConstructor
public class TranscriptionPermissionEvaluator {

  private final TranscriptionService transcriptionService;

  public boolean ownerUserAccess(Authentication authentication, Long id) {
    if (authentication != null
        && authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {

      User currentUser = userDetails.getUser();

      try {
        return transcriptionService.findById(id).getId().equals(currentUser.getId());
      } catch (NotFoundException e) {
        return false;
      }
    }
    return false;
  }

}
