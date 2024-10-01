package project.transcription_application_v2.domain.user.service;

import project.transcription_application_v2.domain.user.entity.User;
import project.transcription_application_v2.infrastructure.exceptions.throwable.BadRequestException;
import project.transcription_application_v2.infrastructure.exceptions.throwable.NotFoundException;
import project.transcription_application_v2.infrastructure.security.dto.CreateUserRequest;
import project.transcription_application_v2.infrastructure.security.dto.MessageResponse;

public interface UserService {

  MessageResponse create(CreateUserRequest createUserRequest) throws BadRequestException;

  User findById(Long id) throws NotFoundException;

  User getLoggedUser();

  void delete(Long id) throws NotFoundException, BadRequestException;

}
