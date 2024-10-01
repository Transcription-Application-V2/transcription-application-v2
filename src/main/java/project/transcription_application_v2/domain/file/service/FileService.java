package project.transcription_application_v2.domain.file.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import project.transcription_application_v2.domain.file.dto.DeletedFilesResponse;
import project.transcription_application_v2.domain.file.dto.FileView;
import project.transcription_application_v2.domain.file.dto.UploadedFilesResponse;
import project.transcription_application_v2.domain.file.entity.File;
import project.transcription_application_v2.infrastructure.exceptions.throwable.BadRequestException;
import project.transcription_application_v2.infrastructure.exceptions.throwable.NotFoundException;

public interface FileService {

  UploadedFilesResponse create(List<MultipartFile> files) throws BadRequestException;

  File findById(Long id) throws NotFoundException;

  DeletedFilesResponse delete(List<Long> ids) throws BadRequestException, NotFoundException;

  void delete(Long id) throws NotFoundException, BadRequestException;

  Page<FileView> getAll(Pageable pageable);

  FileView getById(Long id) throws NotFoundException;

  Page<FileView> getCurrentUsers(Pageable pageable);

  List<File> getAllByCurrentUser(Long id);
}
