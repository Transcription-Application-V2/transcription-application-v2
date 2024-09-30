package project.transcription_application_v2.infrastructure.dropbox_api;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.sharing.ListSharedLinksResult;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import com.dropbox.core.v2.sharing.SharedLinkSettings;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.transcription_application_v2.domain.file.entity.File;
import project.transcription_application_v2.domain.file_meta.service.FileMetaService;
import project.transcription_application_v2.infrastructure.dropbox_api.dto.UploadedDropboxFile;
import project.transcription_application_v2.infrastructure.exceptions.DropboxException;

@Service
@RequiredArgsConstructor
@Slf4j
public class DropboxService {

  // The access_token is short-lived and needs to be manually refreshed every 4 hours without user interaction
  @Value("${dropbox.api.token}")
  private String accessToken;

  private DbxClientV2 client;

  private final FileMetaService fileMetaService;

  @PostConstruct
  public void init() {
    DbxRequestConfig config = DbxRequestConfig.newBuilder("transcription-application-v2").build();
    client = new DbxClientV2(config, accessToken);
  }

  public UploadedDropboxFile upload(MultipartFile file, Long userId) throws DropboxException {
    try {
      String hashedFileName = hashFile(file);
      String filePath = "/" + userId + "/" + hashedFileName;

      try {
        ListFolderResult result = client.files().listFolder("/" + userId);
        for (Metadata metadata : result.getEntries()) {
          if (metadata instanceof FileMetadata && metadata.getName().equals(hashedFileName)) {
            // File exists, check for existing shared links
            ListSharedLinksResult sharedLinksResult = client.sharing().listSharedLinksBuilder()
                .withPath(filePath)
                .withDirectOnly(true)
                .start();
            for (SharedLinkMetadata sharedLink : sharedLinksResult.getLinks()) {
              log.info("Existing shared link found: {}", sharedLink.getUrl());

              return new UploadedDropboxFile(
                  hashedFileName,
                  ensureDownloadUrl(sharedLink.getUrl())
              );
            }
            // No existing shared link found, create a new one
            SharedLinkMetadata sharedLinkMetadata = client.sharing()
                .createSharedLinkWithSettings(filePath, SharedLinkSettings.newBuilder().build());
            return new UploadedDropboxFile(
                hashedFileName,
                ensureDownloadUrl(sharedLinkMetadata.getUrl())
            );
          }
        }
      } catch (DbxException exception) {
        log.warn("Error checking for existing file: {}", exception.getMessage());
      }

      // File does not exist, upload it
      client.files().uploadBuilder(filePath)
          .uploadAndFinish(file.getInputStream());

      log.info("File uploaded successfully");

      return new UploadedDropboxFile(
          hashedFileName,
          ensureDownloadUrl(client.sharing()
              .createSharedLinkWithSettings(filePath, SharedLinkSettings.newBuilder().build())
              .getUrl())
      );

    } catch (DbxException | IOException | NoSuchAlgorithmException | URISyntaxException e) {
      throw new DropboxException(
          "Error uploading file or retrieving metadata: " + e.getLocalizedMessage());
    }
  }

  public void delete(File file, Long userId) throws DropboxException {
    try {
      if (fileMetaService.moreThenOneDropboxDownloadUrls(file.getFileMeta().getDownloadUrl())) {
        log.info("File has more than one download URL, skipping deletion");
        return;
      }
      client
          .files()
          .deleteV2("/" + userId + "/" + file.getFileMeta().getName());
      log.info("File deleted successfully");
    } catch (DbxException exception) {
      log.error("Error deleting file: {}", exception.getMessage());
      throw new DropboxException("Error deleting file: " + exception.getMessage());
    }
  }

  private String ensureDownloadUrl(String url) throws URISyntaxException {
    URI uri = new URI(url);
    URIBuilder uriBuilder = new URIBuilder(uri);
    uriBuilder.setParameter("dl", "1");
    return uriBuilder.build().toString();
  }

  private String hashFile(MultipartFile file) throws IOException, NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    try (InputStream inputStream = file.getInputStream()) {
      byte[] byteArray = new byte[1024];
      int bytesCount = 0;
      while ((bytesCount = inputStream.read(byteArray)) != -1) {
        digest.update(byteArray, 0, bytesCount);
      }
    }
    byte[] bytes = digest.digest();
    return Hex.encodeHexString(bytes);
  }
}
