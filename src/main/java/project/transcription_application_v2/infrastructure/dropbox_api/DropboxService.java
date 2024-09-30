package project.transcription_application_v2.infrastructure.dropbox_api;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.sharing.CreateSharedLinkWithSettingsErrorException;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import com.dropbox.core.v2.sharing.SharedLinkSettings;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.transcription_application_v2.domain.file.entity.File;
import project.transcription_application_v2.infrastructure.exceptions.DropboxException;

@Service
@RequiredArgsConstructor
@Slf4j
public class DropboxService {

  // The access_token is short-lived and needs to be manually refreshed every 4 hours without user interaction
  @Value("${dropbox.api.token}")
  private String accessToken;

  private DbxClientV2 client;

  @PostConstruct
  public void init() {
    DbxRequestConfig config = DbxRequestConfig.newBuilder("transcription-application-v2").build();
    client = new DbxClientV2(config, accessToken);
  }

  public String upload(MultipartFile file) throws DropboxException {
    try {
      // Upload the file to Dropbox
      FileMetadata metadata = client.files().uploadBuilder("/" + file.getOriginalFilename())
          .uploadAndFinish(file.getInputStream());

      // Try to create a shared link
      SharedLinkMetadata sharedLinkMetadata = client.sharing()
          .createSharedLinkWithSettings(metadata.getPathLower(),
              SharedLinkSettings.newBuilder().build());

      return sharedLinkMetadata.getUrl();
    } catch (CreateSharedLinkWithSettingsErrorException e) {
      if (e.errorValue.isSharedLinkAlreadyExists()) {
        // If the shared link already exists, retrieve the existing link
        SharedLinkMetadata sharedLinkMetadata = e.errorValue.getSharedLinkAlreadyExistsValue()
            .getMetadataValue();
        return sharedLinkMetadata.getUrl();
      } else {
        throw new DropboxException(
            "Error uploading file or generating shared link: " + e.getMessage());
      }
    } catch (DbxException | IOException e) {
      throw new DropboxException(
          "Error uploading file or generating shared link: " + e.getMessage());
    }
  }

  public void delete(File file) throws DropboxException {
    try {
      client
          .files()
          .deleteV2("/" + file.getFileMeta().getName());
    } catch (DbxException exception) {
      log.error("Error deleting file: {}", exception.getMessage());
      throw new DropboxException("Error deleting file: " + exception.getMessage());
    }
  }

  public void print() {
    try {
      ListFolderResult result = client.files().listFolder("");
      for (Metadata metadata : result.getEntries()) {
        log.info("File found: {}", metadata.getName());
      }
    } catch (DbxException exception) {
      log.info("File not found: {}", exception.getMessage());
    }
  }
}
