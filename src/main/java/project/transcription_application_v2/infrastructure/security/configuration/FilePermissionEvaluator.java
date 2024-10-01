package project.transcription_application_v2.infrastructure.security.configuration;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import project.transcription_application_v2.domain.file.entity.File;
import project.transcription_application_v2.domain.file.service.FileService;
import project.transcription_application_v2.domain.user.entity.User;
import project.transcription_application_v2.infrastructure.security.entity.UserDetailsImpl;

@Component(value = "filePermissionEvaluator")
@RequiredArgsConstructor
public class FilePermissionEvaluator {

  private final FileService fileService;

  /**
   * This method retrieves the currently logged-in user and verifies if all provided IDs belong to
   * this user. If any of the IDs do not belong to the logged-in user, access to the method will be
   * denied.
   *
   * @param authentication the authentication object containing the user's details
   * @param id             the list of IDs to be checked
   * @return true if all IDs belong to the logged-in user, false otherwise
   */
  public boolean ownerUserAccess(Authentication authentication, List<Long> id) {
    if (authentication != null
        && authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {

      User currentUser = userDetails.getUser();

      return fileService.getAllByCurrentUser(currentUser.getId())
          .stream()
          .map(File::getId)
          .allMatch(id::contains);
    }
    return false;
  }

  /**
   * This method retrieves the currently logged-in user and verifies if the provided ID belongs to
   * this user. If the ID does not belong to the logged-in user, access to the method will be
   * denied.
   *
   * @param authentication the authentication object containing the user's details
   * @param id             the ID to be checked
   * @return true if the ID belongs to the logged-in user, false otherwise
   */
  public boolean ownerUserAccess(Authentication authentication, Long id) {
    if (authentication != null
        && authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {

      User currentUser = userDetails.getUser();

      return fileService.getAllByCurrentUser(currentUser.getId())
          .stream()
          .map(File::getId)
          .anyMatch(id::equals);
    }
    return false;
  }
}
