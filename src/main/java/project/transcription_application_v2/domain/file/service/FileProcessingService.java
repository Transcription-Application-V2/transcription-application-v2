package project.transcription_application_v2.domain.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.transcription_application_v2.domain.file.dto.DeletedFilesResponse;
import project.transcription_application_v2.domain.file.dto.UploadedFilesResponse;
import project.transcription_application_v2.domain.file.entity.File;
import project.transcription_application_v2.domain.file_meta.entity.FileMeta;
import project.transcription_application_v2.domain.file_meta.service.FileMetaService;
import project.transcription_application_v2.domain.paragraph.entity.Paragraph;
import project.transcription_application_v2.domain.paragraph.service.ParagraphService;
import project.transcription_application_v2.domain.transcription.entity.Transcription;
import project.transcription_application_v2.domain.transcription.service.TranscriptionService;
import project.transcription_application_v2.infrastructure.assembly_api.dto.AssemblyConvertedFile;
import project.transcription_application_v2.infrastructure.assembly_api.service.AssemblyService;
import project.transcription_application_v2.infrastructure.assembly_api.service.DropboxService;
import project.transcription_application_v2.infrastructure.exceptions.AssemblyAIException;
import project.transcription_application_v2.infrastructure.exceptions.BadResponseException;
import project.transcription_application_v2.infrastructure.exceptions.DropboxException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileProcessingService {

  private final DropboxService dropboxService;
  private final AssemblyService assemblyService;
  private final FileMetaService fileMetaService;
  private final ParagraphService paragraphService;
  private final TranscriptionService transcriptionService;
  private final FileService fileService;

  public UploadedFilesResponse process(List<MultipartFile> files) {

    List<String> processedFiles = new ArrayList<>();
    List<String> unprocessedFiles = new ArrayList<>();

    for (MultipartFile multipartFile : files) {
      try {
        // Upload the file to Dropbox storage and retrieve the download URL
        String downloadUrl = dropboxService.upload(multipartFile);

        // Using the download URL upload and transcribe the file using Assembly AI
        AssemblyConvertedFile assemblyConvertedFile = assemblyService.transcribe(downloadUrl);

        // Create FileMeta object using the file also giving it a download URL and Assembly AI id
        FileMeta fileMeta = fileMetaService.create(multipartFile, assemblyConvertedFile.getDownloadUrl(), assemblyConvertedFile.getAssemblyId());

        // Use the Transcript from Assembly AI to create Paragraph objects
        List<Paragraph> paragraphs = paragraphService.create(assemblyConvertedFile.getTranscript());

        // Create Transcription object using the Paragraph objects we created
        Transcription transcription = transcriptionService.create(multipartFile.getOriginalFilename(), paragraphs, assemblyConvertedFile.getTranscript());

        // Create File object and map it to FileMeta and Transcription objects, then save them to the Database using Cascade
        fileService.create(fileMeta, transcription);

        processedFiles.add(multipartFile.getOriginalFilename());

      } catch (DropboxException | AssemblyAIException exception) {
        log.error("Error processing files: {}", exception.getMessage());
        unprocessedFiles.add(multipartFile.getOriginalFilename());
      }
    }

    return new UploadedFilesResponse(processedFiles, unprocessedFiles);
  }

  public DeletedFilesResponse delete(List<Long> ids) {

    List<String> deletedFiles = new ArrayList<>();
    List<Long> failedIds = new ArrayList<>();

    for(Long id : ids){
      try {
        // Gets the file by id
        File file = fileService.get(id);

        // First deletes it from the Dropbox storage
        dropboxService.delete(file);

        // Secondly removes the Transcript from Assembly storage
        assemblyService.deleteById(file.getFileMeta().getAssemblyAiId());

        // Finally removes Entities from Database storage
        fileService.delete(file);

        deletedFiles.add(file.getFileMeta().getName());
      } catch (BadResponseException | DropboxException | AssemblyAIException exception) {
        log.error("Error deleting file: {}", exception.getMessage());
        failedIds.add(id);
      }
    }

    return new DeletedFilesResponse(deletedFiles, failedIds);
  }
}
