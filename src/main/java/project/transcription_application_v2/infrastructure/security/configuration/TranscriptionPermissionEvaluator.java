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

  public boolean ownerUserAccess(Authentication authentication, Long id) throws NotFoundException {
    if (authentication != null
        && authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {

      User currentUser = userDetails.getUser();
      User transcriptionOwner = transcriptionService.findById(id)
          .getFile()
          .getUser();

      return currentUser.equals(transcriptionOwner);
    }
    return false;
  }

}
