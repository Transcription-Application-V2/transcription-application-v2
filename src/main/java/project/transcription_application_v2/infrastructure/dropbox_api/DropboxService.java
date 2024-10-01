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

  /**
   * Uploads a file to Dropbox for the specified user. If the file already exists, it checks for
   * existing shared links. If no shared link exists, it creates a new one. If the file does not
   * exist, it uploads the file and creates a shared link.
   *
   * @param file   the file to be uploaded
   * @param userId the ID of the user uploading the file
   * @return an UploadedDropboxFile containing the file name and download URL
   * @throws DropboxException if an error occurs during the upload process
   */
  public UploadedDropboxFile upload(MultipartFile file, Long userId) throws DropboxException {
    try {
      String hashedFileName = hashFile(file);
      String filePath = "/" + userId + "/" + hashedFileName;

      try {
        ListFolderResult result = client.files().listFolder("/" + userId);
        for (Metadata metadata : result.getEntries()) {
          if (metadata instanceof FileMetadata && metadata.getName().equals(hashedFileName)) {
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

  /**
   * Deletes a file from Dropbox for the specified user. If the user uses this download URL more
   * than ones, it skips the deletion.
   *
   * @param file   the file to be deleted
   * @param userId the ID of the user who owns the file
   * @throws DropboxException if an error occurs during the deletion process
   */
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

  /**
   * Ensures the download URL is correctly formatted by manually replacing the "dl" parameter from 0
   * to 1. The provided replace method from the Dropbox API does not work as expected.
   *
   * @param url the original URL
   * @return the modified URL with "dl" parameter set to 1
   * @throws URISyntaxException if the URL is not correctly formatted
   */
  private String ensureDownloadUrl(String url) throws URISyntaxException {
    URI uri = new URI(url);
    URIBuilder uriBuilder = new URIBuilder(uri);
    uriBuilder.setParameter("dl", "1");
    return uriBuilder.build().toString();
  }

  /**
   * Hashes the file using SHA-256 to create a unique hash for the specific file. If there are
   * identical files, the hash will be the same; otherwise, the hash will be different.
   *
   * @param file the file to be hashed
   * @return the SHA-256 hash of the file as a hexadecimal string
   * @throws IOException              if an I/O error occurs
   * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available
   */
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
