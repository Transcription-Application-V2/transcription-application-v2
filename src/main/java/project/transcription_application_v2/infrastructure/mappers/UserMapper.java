package project.transcription_application_v2.infrastructure.mappers;

import org.springframework.stereotype.Component;
import project.transcription_application_v2.domain.user.entity.User;
import project.transcription_application_v2.infrastructure.security.dto.CreateUserRequest;

@Component
public class UserMapper {

  public User toUser(CreateUserRequest createUserRequest){
    User user = new User();
    user.setUsername(createUserRequest.getUsername());
    user.setPassword(createUserRequest.getPassword());
    user.setEmail(createUserRequest.getEmail());
    user.setFirstName(createUserRequest.getFirstName());
    user.setLastName(createUserRequest.getLastName());
    return user;
  }

}
