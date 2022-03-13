package com.github.com.mk868.gateway.auditlog.mapper;

import com.github.com.mk868.gateway.auditlog.dto.LogEntryDto;
import com.github.com.mk868.gateway.auditlog.dto.LogFileDto;
import com.github.com.mk868.gateway.auditlog.model.AuditLog;
import com.github.com.mk868.gateway.auditlog.model.LogFile;
import com.github.com.mk868.gateway.mapper.AppMapperConfig;
import java.time.Instant;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = AppMapperConfig.class)
public interface AuditLogMapper {

  LogEntryDto mapLogEntry(AuditLog logEntry);

  List<LogEntryDto> mapLogEntries(List<AuditLog> logEntry);

  LogFileDto mapLogFile(LogFile file);

  List<LogFileDto> mapLogFiles(List<LogFile> files);

  default Long instantToLong(Instant i) {
    if (i == null) {
      return null;
    }
    return i.toEpochMilli();
  }

}
