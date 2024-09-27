package project.transcription_application_v2.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.transcription_application_v2.domain.user.entity.User;
import project.transcription_application_v2.domain.user.enums.RoleName;
import project.transcription_application_v2.domain.user.repository.UserRepository;
import project.transcription_application_v2.infrastructure.exceptions.BadResponseException;
import project.transcription_application_v2.infrastructure.mappers.UserMapper;
import project.transcription_application_v2.infrastructure.security.dto.CreateUserRequest;
import project.transcription_application_v2.infrastructure.security.dto.MessageResponse;
import project.transcription_application_v2.infrastructure.security.entity.UserDetailsImpl;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final UserMapper userMapper;

  public MessageResponse create(CreateUserRequest createUserRequest) throws BadResponseException {
    if(emailExists(createUserRequest.getEmail()))
      throw new BadResponseException("Email already exists");

    if(usernameExists(createUserRequest.getUsername()))
      throw new BadResponseException("Username already exists");

    User user = userMapper.toUser(createUserRequest);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole(RoleName.USER);
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());

    this.save(user);

    return new MessageResponse("Successfully created user with username: " + user.getUsername());
  }

  public User findById(Long id){
      return this.userRepository.findById(id).orElse(null);
  }

  public void save(User user) {
    userRepository.save(user);
  }

  public User getLoggedUser() {
    UserDetailsImpl loggedUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return loggedUser.getUser();
  }

  public boolean usernameExists(String username) {
    return userRepository.findByUsername(username).isPresent();
  }

  public boolean emailExists(String email) {
    return userRepository.findByEmail(email).isPresent();
  }


}
