package project.transcription_application_v2.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transcription_application_v2.domain.file.entity.File;
import project.transcription_application_v2.domain.file.service.FileService;
import project.transcription_application_v2.domain.user.entity.User;
import project.transcription_application_v2.domain.user.repository.UserRepository;
import project.transcription_application_v2.infrastructure.exceptions.AssemblyAIException;
import project.transcription_application_v2.infrastructure.exceptions.BadResponseException;
import project.transcription_application_v2.infrastructure.exceptions.DropboxException;
import project.transcription_application_v2.infrastructure.exceptions.NotFoundException;
import project.transcription_application_v2.infrastructure.mappers.UserMapper;
import project.transcription_application_v2.infrastructure.security.dto.CreateUserRequest;
import project.transcription_application_v2.infrastructure.security.dto.MessageResponse;
import project.transcription_application_v2.infrastructure.security.entity.UserDetailsImpl;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private FileService fileService;

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  @Autowired
  public void setFileService(@Lazy FileService fileService) {
    this.fileService = fileService;
  }

  public MessageResponse create(CreateUserRequest createUserRequest) throws BadResponseException {
    User savedUser = userRepository.save(userMapper.toEntity(createUserRequest));

    return new MessageResponse(
        "Successfully created user with username: " + savedUser.getUsername()
    );
  }

  public User findById(Long id) throws NotFoundException {
    return userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("User with id: " + id + " not found"));
  }

  public User getLoggedUser() {
    UserDetailsImpl loggedUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return loggedUser.getUser();
  }

  @Override
  @Transactional
  public void delete(Long id) throws NotFoundException, DropboxException, AssemblyAIException {
    User userToDelete = findById(id);

    for (File file : userToDelete.getFiles()) {
      fileService.delete(file.getId());
    }
  }
}
