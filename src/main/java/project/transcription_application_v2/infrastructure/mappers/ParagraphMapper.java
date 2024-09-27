package project.transcription_application_v2.infrastructure.mappers;

import org.springframework.stereotype.Component;
import project.transcription_application_v2.domain.paragraph.dto.ParagraphView;
import project.transcription_application_v2.domain.paragraph.entity.Paragraph;

import java.util.ArrayList;
import java.util.List;

@Component
public class ParagraphMapper {

  public ParagraphView toParagraphView(Paragraph paragraph) {
    return new ParagraphView(
        paragraph.getId(),
        paragraph.getSpeaker(),
        paragraph.getTime(),
        paragraph.getText());
  }

  public List<ParagraphView> toParagraphViewList(List<Paragraph> paragraphs){
    List<ParagraphView> paragraphViews = new ArrayList<>();
    for (Paragraph paragraph : paragraphs) {
      paragraphViews.add(toParagraphView(paragraph));
    }
    return paragraphViews;
  }
}
