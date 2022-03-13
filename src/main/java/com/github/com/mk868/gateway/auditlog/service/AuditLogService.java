package com.github.com.mk868.gateway.auditlog.service;

import com.github.com.mk868.gateway.auditlog.model.AuditLog;
import com.github.com.mk868.gateway.auditlog.model.LogFile;
import com.github.com.mk868.loglib.FileReader;
import com.github.com.mk868.loglib.model.AnonymousFileEntry;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

  private static final Logger log = LoggerFactory.getLogger(AuditLogService.class);
  public static final String LOG_EXTENSION = ".alog";

  private final MessageResolverService messageResolverService;

  @Value("${audit.dir}")
  private String auditDir;

  public AuditLogService(MessageResolverService messageResolverService) {
    this.messageResolverService = messageResolverService;
  }


  public boolean deleteFile(String filename) {
    failOnUnsafeFilename(filename);
    var path = Path.of(auditDir, filename);
    if (!path.toFile().exists()) {
      return true;
    }
    try {
      Files.delete(path);
    } catch (IOException e) {
      log.error("Cannot delete file", e);
      return false;
    }
    return true;
  }

  public List<AuditLog> getLogs(String filename, Locale locale) {
    failOnUnsafeFilename(filename);

    try (var fileReader = new FileReader(new File(filename))) {
      return fileReader.readAllEntries().stream()
          .map(l -> this.map(l, locale))
          .toList();
    } catch (IOException e) {
      log.error("cannot read from file");
    }
    return new ArrayList<>();
  }

  private AuditLog map(AnonymousFileEntry entry, Locale locale) {
    var logId = entry.getLogId();
    var createdAt = entry.getCreatedAt();
    var fallback = false;

    Map<String, Object> tmpParams = new HashMap<>(entry.getParams());
    var messageOpt = messageResolverService.getMessage(logId, tmpParams, locale);

    String message;
    if (messageOpt.isPresent()) {
      message = messageOpt.get();
    } else {
      message = getFallbackMessage(entry);
      fallback = true;
    }

    return new AuditLog(createdAt, message, fallback);
  }

  protected String getFallbackMessage(AnonymousFileEntry entry) {
    var str = entry.getLogId();
    if (!entry.getParams().isEmpty()) {
      str += ": ";
      str += entry.getParams().entrySet().stream()
          .map(kv -> kv.getKey() + "='" + kv.getValue() + "'")
          .collect(Collectors.joining(", "));
    }
    return str;
  }

  public List<LogFile> getFiles() {
    try (var stream = Files.list(Path.of(auditDir))) {
      return stream
          .map(Path::toFile)
          .filter(f -> f.getName().endsWith(LOG_EXTENSION))
          .map(this::map)
          .toList();
    } catch (IOException e) {
      log.error("Cannot read log file list", e);
    }
    return new ArrayList<>();
  }

  public Optional<LogFile> findFile(String filename) {
    failOnUnsafeFilename(filename);
    var file = Path.of(auditDir, filename).toFile();
    if (!file.exists()) {
      return Optional.empty();
    }

    return Optional.of(map(file));
  }

  private LogFile map(File file) {
    return new LogFile(file.getName(), Instant.ofEpochMilli(file.lastModified()), file.length());
  }

  /**
   * Throw exception when filename invalid, this method is here for security reasons. It prevents
   * passing paths in filename fields, like '../../../root/something'.
   *
   * @param filename filename, not path
   */
  private void failOnUnsafeFilename(String filename) {
    if (filename.contains("/") || filename.contains("\\")) {
      throw new IllegalArgumentException("Filename value unsafe");
    }
  }
}
