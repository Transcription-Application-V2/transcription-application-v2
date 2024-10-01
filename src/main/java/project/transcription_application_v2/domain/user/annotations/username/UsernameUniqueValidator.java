package project.transcription_application_v2.domain.user.annotations.username;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import project.transcription_application_v2.domain.user.repository.UserRepository;

public class UsernameUniqueValidator implements ConstraintValidator<UsernameUnique, String> {

  @Autowired
  private UserRepository userRepository;

  @Override
  public void initialize(UsernameUnique constraintAnnotation) {
  }

  @Override
  public boolean isValid(String username, ConstraintValidatorContext context) {
    return username != null && userRepository.findByUsername(username).isEmpty();
  }
}