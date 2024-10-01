package project.transcription_application_v2.domain.file.annotations.not_empty_list_ids;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NotEmptyIdListValidator.class)
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyIdList {
  String message() default "ID list must not be empty";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
