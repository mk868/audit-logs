package com.github.com.mk868.gateway.auditlog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.com.mk868.gateway.auditlog.log.AuditLogId;
import com.github.com.mk868.loglib.FileWriter;
import com.github.com.mk868.loglib.model.FileEntry;
import java.time.Instant;
import java.time.InstantSource;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {
    AuditLogger.class
})
class AuditLoggerTest {

  @Autowired
  AuditLogger auditLogger;
  @MockBean
  FileWriter fileWriter;
  @MockBean
  InstantSource instantSource;

  @Test
  void shouldNotThrowWhenNoLogIdAnnotation() {
    auditLogger.log(new missingIdLog());

    verify(fileWriter, never()).append(any());
  }

  @Test
  void shouldNotThrowWhenNullLog() {
    auditLogger.log(null);

    verify(fileWriter, never()).append(any());
  }

  @Test
  void shouldAppendFileEntry() {
    when(instantSource.instant()).thenReturn(Instant.ofEpochMilli(1644000000000L));
    when(instantSource.millis()).thenCallRealMethod();
    var log = new testLog("generator1", 44);
    auditLogger.log(log);

    ArgumentCaptor<FileEntry> argument = ArgumentCaptor.forClass(FileEntry.class);
    verify(fileWriter).append(argument.capture());
    var fileEntry = argument.getValue();
    assertEquals("TEST_LOG", fileEntry.getLogId());
    assertEquals(Instant.ofEpochMilli(1644000000000L), fileEntry.getCreatedAt());
    assertEquals(log, fileEntry.getParams());
  }

  record missingIdLog() {

  }

  @AuditLogId("TEST_LOG")
  record testLog(String moduleId, int alarmId) {

  }

}
