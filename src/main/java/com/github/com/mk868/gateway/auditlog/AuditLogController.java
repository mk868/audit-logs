package com.github.com.mk868.gateway.auditlog;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.github.com.mk868.gateway.auditlog.dto.LogEntryDto;
import com.github.com.mk868.gateway.auditlog.dto.LogFileDto;
import com.github.com.mk868.gateway.auditlog.mapper.AuditLogMapper;
import com.github.com.mk868.gateway.auditlog.service.AuditLogService;
import java.util.Locale;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/audit-logs")
public class AuditLogController {

  // HATEOAS
  private static final String REL_ALL_FILES = "allFiles";
  private static final String REL_FILE = "file";
  private static final String REL_LOG_ENTRIES = "logEntries";

  public final AuditLogService auditLogService;
  public final AuditLogMapper auditLogMapper;

  public AuditLogController(AuditLogService auditLogService, AuditLogMapper auditLogMapper) {
    this.auditLogService = auditLogService;
    this.auditLogMapper = auditLogMapper;
  }

  @GetMapping
  public CollectionModel<LogFileDto> listFiles() {
    var result = auditLogMapper.mapLogFiles(auditLogService.getFiles());

    result.forEach(f -> f.add(
        linkTo(methodOn(AuditLogController.class).getFile(f.getFilename())).withSelfRel(),
        linkTo(methodOn(AuditLogController.class).getLogFileEntries(f.getFilename()))
            .withRel(REL_LOG_ENTRIES)
    ));

    return CollectionModel.of(result,
        linkTo(methodOn(AuditLogController.class).listFiles()).withSelfRel()
    );
  }

  @GetMapping("{filename}")
  public HttpEntity<LogFileDto> getFile(@PathVariable String filename) {
    var fileOpt = auditLogService.findFile(filename);
    if (fileOpt.isEmpty()) {
      return ResponseEntity.notFound()
          .build();
    }

    var result = auditLogMapper.mapLogFile(fileOpt.get());
    result.add(
        linkTo(methodOn(AuditLogController.class).getFile(filename)).withSelfRel(),
        linkTo(methodOn(AuditLogController.class).listFiles()).withRel(REL_ALL_FILES),
        linkTo(methodOn(AuditLogController.class).getLogFileEntries(filename))
            .withRel(REL_LOG_ENTRIES)
    );

    return ResponseEntity.ok(result);
  }

  @DeleteMapping("{filename}")
  public ResponseEntity<?> deleteFile(@PathVariable String filename) {
    auditLogService.deleteFile(filename);

    return ResponseEntity.noContent()
        .build();
  }

  @GetMapping("{filename}/entries")
  public ResponseEntity<CollectionModel<LogEntryDto>> getLogFileEntries(
      @PathVariable String filename) {
    var fileOpt = auditLogService.findFile(filename);
    if (fileOpt.isEmpty()) {
      return ResponseEntity.notFound()
          .build();
    }

    Locale locale = LocaleContextHolder.getLocale();
    var result = auditLogMapper.mapLogEntries(auditLogService.getLogs(filename, locale));

    return ResponseEntity.ok(CollectionModel.of(result,
        linkTo(methodOn(AuditLogController.class).getLogFileEntries(filename)).withSelfRel(),
        linkTo(methodOn(AuditLogController.class).getFile(filename)).withRel(REL_FILE),
        linkTo(methodOn(AuditLogController.class).listFiles()).withRel(REL_ALL_FILES)
    ));
  }
}
