package project.transcription_application_v2.domain.transcription.dto;

import project.transcription_application_v2.domain.paragraph.dto.ParagraphView;

import java.time.LocalDateTime;
import java.util.List;

public record TranscriptionView(
    Long id,
    String name,
    Long size,
    LocalDateTime publishedAt,
    LocalDateTime lastModifiedAt,
    List<ParagraphView> paragraphs
) {}