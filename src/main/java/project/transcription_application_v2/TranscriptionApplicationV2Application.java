package project.transcription_application_v2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TranscriptionApplicationV2Application {

  public static void main(String[] args) {
    SpringApplication.run(TranscriptionApplicationV2Application.class, args);
  }

  /* TODO:
      0. Implement MapStruct over ModelMapper
      1. Test uploading files with existing file name
      2. Test delete files - DONE
      3. Create get all files - DONE
      4. Create get current user's all files - DONE
      5. Implement Pagination on both - DONE
      6. Create update and delete Transcription
      7. Create update and delete Paragraph
      8. (BUG) Transcription from Assembly returning null Utterance, probably a problem with how the download url is generated
   */


}
