package project.transcription_application_v2.infrastructure.assembly_api.service;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.transcription_application_v2.infrastructure.exceptions.DropboxException;

@Service
public class DropboxService {

  private final static Logger logger = LoggerFactory.getLogger(DropboxService.class);

  private final static String DROPBOX_PATH = "/transcription-application-v2/";

  @Value("${dropbox.api.access_token}")
  private String accessToken;

  private DbxClientV2 client;


  @PostConstruct
  public void init() {
    DbxRequestConfig config = DbxRequestConfig.newBuilder("transcription-application-v2/1.0").build();
    client = new DbxClientV2(config, accessToken);
  }

  // When copying function is commented the API gives back error -> Token is malformed
  public String upload(MultipartFile file) throws DropboxException {
    try {
      // Checks if file is already uploaded
//      Metadata metadata = client.files().getMetadata(DROPBOX_PATH + file.getOriginalFilename());

      String path = DROPBOX_PATH + file.getOriginalFilename();

      // If it is a copy is made, only one copy is allowed, every next one is getting replaced
//      if (metadata != null)
//        path = path + "_copy";

      FileMetadata uploadedFile = client
          .files()
          .uploadBuilder(path)
          .uploadAndFinish(file.getInputStream());

      // Create and return the shared link (download urL)
      return client
          .sharing()
          .createSharedLinkWithSettings(uploadedFile.getPathLower())
          .getUrl()
          .replace("?dl=0", "?dl=1"); // Returns the direct download link, which is needed for Assembly to work properly
    } catch (Exception exception) {
      logger.error("Unable to upload file to dropbox: {}", exception.getMessage());
      throw new DropboxException("Unable to upload file to dropbox: {}" + exception.getMessage());
    }
  }

}
