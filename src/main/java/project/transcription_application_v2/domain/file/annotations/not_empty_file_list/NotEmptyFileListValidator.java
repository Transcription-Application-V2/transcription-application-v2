package project.transcription_application_v2.domain.file.annotations.not_empty_file_list;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class NotEmptyFileListValidator implements
    ConstraintValidator<NotEmptyFileList, List<MultipartFile>> {

  @Override
  public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
    return files != null && !files.isEmpty();
  }
}