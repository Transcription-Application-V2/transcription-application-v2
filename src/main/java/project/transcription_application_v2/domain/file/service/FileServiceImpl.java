package project.transcription_application_v2.domain.file.service;

import static project.transcription_application_v2.infrastructure.exceptions.ExceptionMessages.FILE_NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.transcription_application_v2.domain.file.dto.DeletedFilesResponse;
import project.transcription_application_v2.domain.file.dto.FileView;
import project.transcription_application_v2.domain.file.dto.UploadedFilesResponse;
import project.transcription_application_v2.domain.file.entity.File;
import project.transcription_application_v2.domain.file.repository.FileRepository;
import project.transcription_application_v2.domain.file_meta.dto.CreateFileMeta;
import project.transcription_application_v2.domain.file_meta.service.FileMetaService;
import project.transcription_application_v2.domain.transcription.dto.CreateTranscription;
import project.transcription_application_v2.domain.transcription.service.TranscriptionService;
import project.transcription_application_v2.domain.user.entity.User;
import project.transcription_application_v2.domain.user.service.UserService;
import project.transcription_application_v2.infrastructure.assembly_api.dto.AssemblyConvertedFile;
import project.transcription_application_v2.infrastructure.assembly_api.service.AssemblyService;
import project.transcription_application_v2.infrastructure.dropbox_api.DropboxService;
import project.transcription_application_v2.infrastructure.dropbox_api.dto.UploadedDropboxFile;
import project.transcription_application_v2.infrastructure.exceptions.throwable.BadRequestException;
import project.transcription_application_v2.infrastructure.exceptions.throwable.NotFoundException;
import project.transcription_application_v2.infrastructure.mappers.FileMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

  private final FileRepository fileRepository;

  private final UserService userService;
  private final DropboxService dropboxService;
  private final AssemblyService assemblyService;
  private final FileMetaService fileMetaService;
  private final TranscriptionService transcriptionService;

  private final FileMapper fileMapper;

  /**
   * Processes a list of multipart files by uploading them to Dropbox, transcribing them using
   * Assembly AI, and saving the resulting metadata and transcription to the database.
   *
   * <p>Steps involved:
   * <ul>
   *   <li>Retrieve the logged-in user.</li>
   *   <li>Save a new file entity associated with the logged-in user.</li>
   *   <li>Upload the file to Dropbox storage and retrieve the download URL.</li>
   *   <li>Using the download URL, upload and transcribe the file using Assembly AI.</li>
   *   <li>Create a FileMeta object using the file, download URL, and Assembly AI ID.</li>
   *   <li>Create a Transcription object using the transcript from Assembly AI.</li>
   *   <li>Save the FileMeta and Transcription objects to the database.</li>
   * </ul>
   * </p>
   *
   * @param files the list of multipart files to be processed
   * @return an UploadedFilesResponse containing lists of successfully processed and unprocessed files
   */
  @Override
  public UploadedFilesResponse create(List<MultipartFile> files) {
    List<String> processedFiles = new ArrayList<>();
    List<String> unprocessedFiles = new ArrayList<>();
    User loggedUser = userService.getLoggedUser();

    for (MultipartFile multipartFile : files) {
      try {
        UploadedDropboxFile uploadedDropboxFile = dropboxService.upload(multipartFile , loggedUser.getId());
        AssemblyConvertedFile assemblyConvertedFile = assemblyService.transcribe(uploadedDropboxFile.url());

        File file = new File();
        file.setUser(loggedUser);
        File savedFile = fileRepository.save(file);

        CreateFileMeta fileMetaDto = new CreateFileMeta(
            multipartFile,
            uploadedDropboxFile.name(),
            assemblyConvertedFile.getDownloadUrl(),
            assemblyConvertedFile.getAssemblyId(),
            savedFile.getId()
        );

        CreateTranscription transcriptionDto = new CreateTranscription(
            multipartFile.getOriginalFilename(),
            assemblyConvertedFile.getTranscript(),
            savedFile.getId()
        );

        fileMetaService.create(fileMetaDto);
        transcriptionService.create(transcriptionDto);

        processedFiles.add(multipartFile.getOriginalFilename());

      } catch (
          NotFoundException |
          BadRequestException exception
      ) {
        log.error("Error processing files: {}", exception.getMessage());
        unprocessedFiles.add(multipartFile.getOriginalFilename());
      }
    }

    return new UploadedFilesResponse(processedFiles, unprocessedFiles);
  }

  @Override
  public FileView getById(Long id) throws NotFoundException {
    return fileMapper.toView(findById(id));
  }

  @Transactional
  @Override
  public DeletedFilesResponse delete(List<Long> ids) {

    List<String> deletedFiles = new ArrayList<>();
    List<Long> failedIds = new ArrayList<>();

    for (Long id : ids) {
      try {
        File file = findById(id);
        delete(id);

        deletedFiles.add(file.getFileMeta().getName());
      } catch (BadRequestException | NotFoundException exception) {
        log.error("Error deleting file: {}", exception.getMessage());
        failedIds.add(id);
      }
    }

    return new DeletedFilesResponse(deletedFiles, failedIds);
  }

  @Override
  public Page<FileView> getAll(Pageable pageable, String group) {
    return Optional.ofNullable(group)
        .map(g -> fileRepository.getAllByGroup(g, pageable))
        .map(files -> files.map(fileMapper::toView))
        .orElse(fileRepository.retrieveAllFiles(pageable).map(fileMapper::toView));
  }

  @Override
  public Page<FileView> getCurrentUsers(Pageable pageable, String group) {
    Long userId = userService.getLoggedUser().getId();

    return Optional.ofNullable(group)
        .map(g -> fileRepository.getAllByGroupAndUser_Id(g, userId, pageable))
        .map(files -> files.map(fileMapper::toView))
        .orElse(fileRepository.retrieveAllFilesByUserId(userId, pageable).map(fileMapper::toView));
  }

  @Override
  @Transactional
  public void delete(Long id) throws NotFoundException, BadRequestException {
    File file = findById(id);

    dropboxService.delete(file , file.getUser().getId());
    assemblyService.deleteById(file.getFileMeta().getAssemblyAiId());
    fileMetaService.delete(file.getFileMeta().getId());
    transcriptionService.delete(file.getTranscription().getId());
    fileRepository.delete(file);
  }

  @Override
  public List<File> getAllByCurrentUser(Long id) {
    return fileRepository.findAllByUserId(id);
  }

  @Override
  public File findById(Long id) throws NotFoundException {
    return fileRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(FILE_NOT_FOUND, id));
  }
}
