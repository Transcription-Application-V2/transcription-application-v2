package project.transcription_application_v2.domain.user.dto;

import java.time.LocalDateTime;
import project.transcription_application_v2.domain.user.enums.RoleName;

public record UserView(
    String username,
    String email,
    String firstName,
    String lastName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    RoleName role
) {

}
