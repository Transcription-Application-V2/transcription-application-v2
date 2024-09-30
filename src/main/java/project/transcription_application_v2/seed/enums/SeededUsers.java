package project.transcription_application_v2.seed.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.transcription_application_v2.domain.user.entity.User;
import project.transcription_application_v2.domain.user.enums.RoleName;

@RequiredArgsConstructor
@Getter
public enum SeededUsers {
  ADMIN("admin", "admin@example.com", "Admin", "User", RoleName.ADMIN),
  USER1("user1", "user1@example.com", "John", "Doe", RoleName.USER),
  USER2("user2", "user2@example.com", "Jane", "Doe", RoleName.USER),
  USER3("user3", "user3@example.com", "Alice", "Smith", RoleName.USER),
  USER4("user4", "user4@example.com", "Bob", "Brown", RoleName.USER);

  // Password is "password"
  private static final String PASSWORD = "password";
  private final String username;
  private final String email;
  private final String firstName;
  private final String lastName;
  private final RoleName role;

  public User toUser() {
    User user = new User();
    user.setUsername(username);
    user.setPassword(PASSWORD);
    user.setEmail(email);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setRole(role);
    return user;
  }
}
