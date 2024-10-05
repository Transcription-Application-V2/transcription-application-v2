package project.transcription_application_v2.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.transcription_application_v2.domain.paragraph.dto.ParagraphView;
import project.transcription_application_v2.domain.paragraph.dto.UpdateParagraph;
import project.transcription_application_v2.domain.paragraph.dto.UpdateSpeakers;
import project.transcription_application_v2.domain.paragraph.service.ParagraphService;
import project.transcription_application_v2.infrastructure.exceptions.throwable.NotFoundException;
import project.transcription_application_v2.infrastructure.openAi.ParagraphControllerDocumentation;

@RestController
@RequestMapping("/api/v2/paragraph")
@RequiredArgsConstructor
public class ParagraphController implements ParagraphControllerDocumentation {

  private final ParagraphService paragraphService;

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN') || @paragraphPermissionEvaluator.ownerUserAccess(authentication, #id)")
  public ResponseEntity<ParagraphView> get(@PathVariable Long id) throws NotFoundException {
    return ResponseEntity.ok(paragraphService.get(id));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN') || @paragraphPermissionEvaluator.ownerUserAccess(authentication, #id)")
  public ResponseEntity<ParagraphView> update(
      @PathVariable Long id,
      @RequestBody @Valid UpdateParagraph dto
  ) throws NotFoundException {
    return ResponseEntity.ok(paragraphService.update(id, dto));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN') || @paragraphPermissionEvaluator.ownerUserAccess(authentication, #id)")
  public ResponseEntity<Void> delete(@PathVariable Long id) throws NotFoundException {
    paragraphService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/speakers")
  @PreAuthorize("hasAnyRole('ADMIN') || @transcriptionPermissionEvaluator.ownerUserAccess(authentication, #dto.fileId())")
  public ResponseEntity<Void> updateAllSpeakers(
      @RequestBody @Valid UpdateSpeakers dto
  ) throws NotFoundException {
    paragraphService.updateSpeakers(dto);
    return ResponseEntity.noContent().build();
  }
}
