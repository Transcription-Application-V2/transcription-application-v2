package project.transcription_application_v2.domain.user.annotations.email;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import project.transcription_application_v2.domain.user.repository.UserRepository;

public class EmailUniqueValidator implements ConstraintValidator<EmailUnique, String> {

  @Autowired
  private UserRepository userRepository;

  @Override
  public void initialize(EmailUnique constraintAnnotation) {
  }

  @Override
  public boolean isValid(String email, ConstraintValidatorContext context) {
    return email != null && userRepository.findByEmail(email).isEmpty();
  }
}