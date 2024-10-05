package project.transcription_application_v2.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.transcription_application_v2.domain.user.dto.UpdateUser;
import project.transcription_application_v2.domain.user.dto.UserView;
import project.transcription_application_v2.domain.user.service.UserService;
import project.transcription_application_v2.infrastructure.exceptions.throwable.BadRequestException;
import project.transcription_application_v2.infrastructure.exceptions.throwable.NotFoundException;
import project.transcription_application_v2.infrastructure.openAi.UserControllerDocumentation;
import project.transcription_application_v2.infrastructure.security.dto.CreateUserRequest;
import project.transcription_application_v2.infrastructure.security.dto.MessageResponse;

@RestController
@RequestMapping("/api/v2/user")
@RequiredArgsConstructor
public class UserController implements UserControllerDocumentation {

  private final UserService userService;

  @PostMapping("/create")
  public ResponseEntity<MessageResponse> handleSignUp(
      @RequestBody @Valid CreateUserRequest createUserRequest
  ) throws BadRequestException {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userService.create(createUserRequest));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN') || @userPermissionEvaluator.ownerUserAccess(authentication, #id)")
  public ResponseEntity<UserView> get(@PathVariable Long id) throws NotFoundException {
    return ResponseEntity.ok(userService.getById(id));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN') || @userPermissionEvaluator.ownerUserAccess(authentication, #id)")
  public ResponseEntity<UserView> update(
      @PathVariable Long id,
      @RequestBody @Valid UpdateUser dto
  ) throws NotFoundException, BadRequestException {
    return ResponseEntity.ok(userService.update(id, dto));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN') || @userPermissionEvaluator.ownerUserAccess(authentication, #id)")
  public ResponseEntity<Void> delete(@PathVariable Long id)
      throws NotFoundException, BadRequestException {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/all")
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<Page<UserView>> getAllUsers(Pageable pageable) {
    return ResponseEntity.ok(userService.getAll(pageable));
  }
}
