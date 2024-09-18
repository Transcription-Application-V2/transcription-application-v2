package project.transcription_application_v2.domain.paragraph.service;

import com.assemblyai.api.resources.transcripts.types.Transcript;
import com.assemblyai.api.resources.transcripts.types.TranscriptUtterance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.transcription_application_v2.domain.paragraph.entity.Paragraph;
import project.transcription_application_v2.domain.paragraph.repository.ParagraphRepository;

import java.util.ArrayList;
import java.util.List;

  @Service
  @RequiredArgsConstructor
  public class ParagraphServiceImpl implements ParagraphService {

    private final ParagraphRepository paragraphRepository;

    public List<Paragraph> create(Transcript transcript) {

      List<Paragraph> paragraphs = new ArrayList<>();

      if(transcript.getUtterances().isPresent()) {
        for(TranscriptUtterance utterance : transcript.getUtterances().get()) {
          paragraphs.add(new Paragraph(utterance.getSpeaker(), (long) utterance.getStart(), utterance.getText(), null));
        }
      }

      return paragraphs;
    }
  }
