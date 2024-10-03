package project.transcription_application_v2.infrastructure.exceptions;

public enum ExceptionMessages {
  USER_NOT_FOUND(
      "User not found with id: %s",
      "User not found"
  ),
  TRANSCRIPTION_NOT_FOUND(
      "Transcription not found with id: %s",
      "Transcription not found"
  ),
  PARAGRAPH_NOT_FOUND(
      "Paragraph not found with id: %s",
      "Paragraph not found"
  ),
  FILE_META_NOT_FOUND(
      "FileMeta not found with id: %s",
      "FileMeta not found"
  ),
  FILE_NOT_FOUND(
      "File not found with id: %s",
      "File not found"
  ),
  ERROR_TRANSCRIBING_FILE(
      "Error transcribing the file: %s",
      "Assembly error"
  ),
  ERROR_DELETING_TRANSCRIPTION(
      "Error deleting transcription: %s",
      "Assembly error"
  ),
  ERROR_FETCHING_TRANSCRIPTION(
      "Error while fetching transcription: %s",
      "Assembly error"
  ),
  ERROR_UPLOADING_FILE(
      "Error uploading file or retrieving metadata: %s",
      "Dropbox error"
  ),
  ERROR_DELETING_FILE(
      "Error deleting file: %s",
      "Dropbox error"
  ),
  AUTHENTICATION_FAILED(
      "Authentication failed: %s",
      "Authentication error"
  ),
  REFRESH_TOKEN_EXPIRED(
      "Refresh token was expired. Please make a new sign-in request",
      "Refresh token error"
  ),
  NO_PARAGRAPHS_FOUND(
      "No paragraphs found for file ID: %s",
      "No paragraphs found"
  );

  public final String message;
  public final String title;

  ExceptionMessages(String message, String title) {
    this.message = message;
    this.title = title;
  }
}
