package com.github.com.mk868.gateway.auditlog;

import com.github.com.mk868.gateway.auditlog.log.AuditLogId;
import com.github.com.mk868.loglib.FileWriter;
import com.github.com.mk868.loglib.model.FileEntry;
import java.time.InstantSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Audit logger class
 */
@Component
public class AuditLogger {

  private static final Logger log = LoggerFactory.getLogger(AuditLogger.class);
  private final InstantSource instantSource;
  private final FileWriter fileWriter;

  public AuditLogger(InstantSource instantSource,
      FileWriter fileWriter) {
    this.instantSource = instantSource;
    this.fileWriter = fileWriter;
    log.info("Creating audit logger");
  }

  /**
   * Log audit message, this method should never throw exception - it should fail silently to not
   * break logic.
   *
   * @param auditLog message object
   */
  public <T extends Record> void log(T auditLog) {
    if (auditLog == null) {
      log.warn("called with null log");
      return;
    }
    var logId = auditLog.getClass().getAnnotation(AuditLogId.class);
    if (logId == null) {
      log.warn("class {} not marked with {} annotation", auditLog.getClass(), AuditLogId.class);
      return;
    }

    var fileEntry = new FileEntry<T>();
    fileEntry.setLogId(logId.value());
    fileEntry.setCreatedAt(instantSource.instant());
    // auditLog is immutable, so we can safely pass it to fileWriter
    fileEntry.setParams(auditLog);

    fileWriter.append(fileEntry);
  }
}
