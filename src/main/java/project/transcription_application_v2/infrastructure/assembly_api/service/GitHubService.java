package project.transcription_application_v2.infrastructure.assembly_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import project.transcription_application_v2.infrastructure.exceptions.GitHubException;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class GitHubService {

  private final static Logger logger = LoggerFactory.getLogger(GitHubService.class);

  @Value("${github.access_token}")
  private String accessToken;

  @Value("${github.profile}")
  private String profile;

  @Value("${github.repository}")
  private String repository;

  private final WebClient webClient = WebClient.builder().baseUrl("https://api.github.com").build();

  private final ObjectMapper objectMapper = new ObjectMapper();

  public String upload(MultipartFile file) throws GitHubException {
    try {
      String encodedContent = Base64.getEncoder().encodeToString(file.getBytes());

      String url = String.format("/repos/%s/%s/contents/%s", profile, repository, file.getOriginalFilename());

      String sha = getFileSha(file.getOriginalFilename());

      // If sha isn't null a file with the given filename exists, which means we need a sha to overwrite it
      String payload = sha == null ?
          String.format("{\"message\":\"Upload file\",\"content\":\"%s\"}", encodedContent) :
          String.format("{\"message\":\"Update file\",\"content\":\"%s\",\"sha\":\"%s\"}", encodedContent, sha);

      String response = webClient.put()
          .uri(url)
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(payload)
          .retrieve()
          .bodyToMono(String.class)
          .block();

      return objectMapper.readTree(response).path("content").path("download_url").asText();
    } catch (Exception exception) {
      logger.error("Error uploading files: {}", exception.getMessage());
      throw new GitHubException("Error uploading files: {}" + exception.getMessage());
    }
  }

  public String getFileSha(String fileName) throws GitHubException {
    try {
      String url = String.format("/repos/%s/%s/contents/%s", profile, repository, fileName);

      String response = webClient.get()
          .uri(url)
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
          .retrieve()
          .bodyToMono(String.class)
          .block();

      return objectMapper.readTree(response).path("sha").asText();
    } catch (Exception exception) {
      if (exception instanceof WebClientResponseException && ((WebClientResponseException) exception).getStatusCode() == HttpStatus.NOT_FOUND) {
        // File does not exist
        return null;
      }
      logger.error("Error retrieving file metadata: {}", exception.getMessage());
      throw new GitHubException("Error retrieving file metadata: " + exception.getMessage());
    }
  }


}
