package project.transcription_application_v2.domain.user.service;

import project.transcription_application_v2.domain.user.entity.User;
import project.transcription_application_v2.infrastructure.exceptions.AssemblyAIException;
import project.transcription_application_v2.infrastructure.exceptions.BadResponseException;
import project.transcription_application_v2.infrastructure.exceptions.DropboxException;
import project.transcription_application_v2.infrastructure.exceptions.NotFoundException;
import project.transcription_application_v2.infrastructure.security.dto.CreateUserRequest;
import project.transcription_application_v2.infrastructure.security.dto.MessageResponse;

public interface UserService {

  MessageResponse create(CreateUserRequest createUserRequest) throws BadResponseException;

  User findById(Long id) throws NotFoundException;

  User getLoggedUser();

  void delete(Long id) throws NotFoundException, DropboxException, AssemblyAIException;

}
