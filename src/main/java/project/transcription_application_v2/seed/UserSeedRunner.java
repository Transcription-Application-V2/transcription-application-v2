package project.transcription_application_v2.seed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import project.transcription_application_v2.domain.user.entity.User;
import project.transcription_application_v2.domain.user.repository.UserRepository;
import project.transcription_application_v2.seed.enums.SeededUsers;

@Component
@Profile("!prod")
@RequiredArgsConstructor
@Order(1)
@Slf4j
public class UserSeedRunner  implements ApplicationRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("Seeding users");

    for (SeededUsers seededUser : SeededUsers.values()) {
      if (!userRepository.existsByUsername(seededUser.getUsername())) {
        User user = seededUser.toUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("Added user: {}", user.getUsername());
      } else {
        log.info("User already exists: {}", seededUser.getUsername());
      }
    }

    log.info("Seeding users completed");
  }
}
