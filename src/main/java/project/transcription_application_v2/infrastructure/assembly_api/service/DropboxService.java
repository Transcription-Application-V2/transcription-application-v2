package project.transcription_application_v2.infrastructure.assembly_api.service;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.transcription_application_v2.infrastructure.exceptions.DropboxException;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DropboxService {

  private final static Logger logger = LoggerFactory.getLogger(DropboxService.class);

  @Value("${dropbox.api.key}")
  private String accessToken;

  private DbxClientV2 client;

  @PostConstruct
  public void init() {
    DbxRequestConfig config = DbxRequestConfig.newBuilder("transcription-application-v2").build();
    client = new DbxClientV2(config, accessToken);
  }

  public String upload(MultipartFile file) throws DropboxException {
    try {
      FileMetadata uploadedFile = client
          .files()
          .uploadBuilder("/" + file.getOriginalFilename())
          .uploadAndFinish(file.getInputStream());

      // Create and return the shared link (download urL)
      return client
          .sharing()
          .createSharedLinkWithSettings(uploadedFile.getPathLower())
          .getUrl()
          .replace("?dl=0", "?dl=1"); // Returns the direct download link
    } catch (DbxException | IOException exception) {
      logger.error("Error uploading file or generating shared link: {}", exception.getMessage());
      throw new DropboxException("Error uploading file or generating shared link" + exception.getMessage());
    }
  }


}
