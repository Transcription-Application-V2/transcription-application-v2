package project.transcription_application_v2.domain.file.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
import project.transcription_application_v2.infrastructure.assembly_api.service.GitHubService;
import project.transcription_application_v2.infrastructure.exceptions.AssemblyAIException;
import project.transcription_application_v2.infrastructure.exceptions.GitHubException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileProcessingService {

  private static final Logger logger = LoggerFactory.getLogger(FileProcessingService.class);

  private final GitHubService gitHubService;
  private final AssemblyService assemblyService;
  private final FileMetaService fileMetaService;
  private final ParagraphService paragraphService;
  private final TranscriptionService transcriptionService;
  private final FileService fileService;

  public UploadedFilesResponse processFiles(List<MultipartFile> files) {

    List<String> processedFiles = new ArrayList<>();
    List<String> unprocessedFiles = new ArrayList<>();

    for (MultipartFile multipartFile : files) {
      try {
        String downloadUrl = gitHubService.upload(multipartFile);

        AssemblyConvertedFile assemblyConvertedFile = assemblyService.transcribe(downloadUrl);

        FileMeta fileMeta = fileMetaService.create(multipartFile, assemblyConvertedFile.getDownloadUrl(), assemblyConvertedFile.getAssemblyId());

        List<Paragraph> paragraphs = paragraphService.create(assemblyConvertedFile.getTranscript());

        Long transcriptionSize = (long) assemblyConvertedFile.getTranscript().getText().get().length();

        Transcription transcription = transcriptionService.create(multipartFile.getOriginalFilename(), paragraphs, transcriptionSize);

        File file = fileService.create(fileMeta, transcription);

        processedFiles.add(multipartFile.getOriginalFilename());

      } catch (GitHubException | AssemblyAIException exception) {
        logger.error("Error processing files: {}", exception.getMessage());
        unprocessedFiles.add(multipartFile.getOriginalFilename());
      }
    }

    return new UploadedFilesResponse(processedFiles, unprocessedFiles);
  }

}
