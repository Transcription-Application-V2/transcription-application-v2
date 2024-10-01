package project.transcription_application_v2.domain.file.annotations.not_empty_file_list;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.hibernate.validator.internal.constraintvalidators.bv.notempty.NotEmptyValidatorForArray;

@Constraint(validatedBy = NotEmptyValidatorForArray.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyFileList {

  String message() default "File list must not be empty";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}