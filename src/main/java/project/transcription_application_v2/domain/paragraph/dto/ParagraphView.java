package project.transcription_application_v2.domain.paragraph.dto;

public record ParagraphView(
    Long id,
    String speaker,
    Long time,
    String text
) {

}