package project.transcription_application_v2.domain.file.annotations.not_empty_list_ids;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class NotEmptyIdListValidator implements ConstraintValidator<NotEmptyIdList, List<Long>> {

  @Override
  public boolean isValid(List<Long> ids, ConstraintValidatorContext context) {
    return ids != null && !ids.isEmpty();
  }
}
