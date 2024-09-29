package project.transcription_application_v2.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.transcription_application_v2.domain.user.entity.User;
import project.transcription_application_v2.domain.user.repository.UserRepository;
import project.transcription_application_v2.infrastructure.exceptions.BadResponseException;
import project.transcription_application_v2.infrastructure.mappers.UserMapper;
import project.transcription_application_v2.infrastructure.security.dto.CreateUserRequest;
import project.transcription_application_v2.infrastructure.security.dto.MessageResponse;
import project.transcription_application_v2.infrastructure.security.entity.UserDetailsImpl;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  public MessageResponse create(CreateUserRequest createUserRequest) throws BadResponseException {
    User savedUser = userRepository.save(userMapper.toEntity(createUserRequest));

    return new MessageResponse(
        "Successfully created user with username: " + savedUser.getUsername()
    );
  }

  public User findById(Long id){
      return userRepository.findById(id).orElse(null);
  }

  public User getLoggedUser() {
    UserDetailsImpl loggedUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return loggedUser.getUser();
  }

}
