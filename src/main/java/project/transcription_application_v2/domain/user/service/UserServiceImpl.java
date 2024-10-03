package project.transcription_application_v2.domain.user.service;

import static project.transcription_application_v2.infrastructure.exceptions.ExceptionMessages.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transcription_application_v2.domain.file.entity.File;
import project.transcription_application_v2.domain.file.service.FileService;
import project.transcription_application_v2.domain.user.dto.UpdateUser;
import project.transcription_application_v2.domain.user.dto.UserView;
import project.transcription_application_v2.domain.user.entity.User;
import project.transcription_application_v2.domain.user.repository.UserRepository;
import project.transcription_application_v2.infrastructure.exceptions.throwable.BadRequestException;
import project.transcription_application_v2.infrastructure.exceptions.throwable.NotFoundException;
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

  public MessageResponse create(CreateUserRequest createUserRequest) throws BadRequestException {
    User savedUser = userRepository.save(userMapper.toEntity(createUserRequest));

    return new MessageResponse(
        "Successfully created user with username: " + savedUser.getUsername()
    );
  }

  public User findById(Long id) throws NotFoundException {
    return userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND, id));
  }

  public User getLoggedUser() {
    UserDetailsImpl loggedUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return loggedUser.getUser();
  }

  @Override
  public UserView getById(Long id) throws NotFoundException {
    return userMapper.toView(findById(id));
  }

  @Override
  public UserView update(Long id, UpdateUser dto) throws NotFoundException {
    User userToUpdate = findById(id);

    userMapper.updateEntity(userToUpdate, dto);

    return userMapper.toView(userRepository.save(userToUpdate));
  }

  @Override
  @Transactional
  public void delete(Long id) throws NotFoundException, BadRequestException {
    User userToDelete = findById(id);

    for (File file : userToDelete.getFiles()) {
      fileService.delete(file.getId());
    }
  }
}
