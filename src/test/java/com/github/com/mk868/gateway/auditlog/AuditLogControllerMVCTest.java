package com.github.com.mk868.gateway.auditlog;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.com.mk868.gateway.ConfigureMappers;
import com.github.com.mk868.gateway.MockMVCTest;
import com.github.com.mk868.gateway.auditlog.model.AuditLog;
import com.github.com.mk868.gateway.auditlog.model.LogFile;
import com.github.com.mk868.gateway.auditlog.service.AuditLogService;
import java.time.Instant;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = AuditLogController.class)
@MockMVCTest
@ConfigureMappers
class AuditLogControllerMVCTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  AuditLogService auditLogService;

  @BeforeEach
  void init() {
    var file1 = new LogFile("file1", Instant.ofEpochMilli(1644681000000L), 10000L);
    var file2 = new LogFile("file2", Instant.ofEpochMilli(1644682000000L), 20000L);
    var file3 = new LogFile("file3", Instant.ofEpochMilli(1644683000000L), 30000L);

    when(auditLogService.getFiles())
        .thenReturn(Lists.newArrayList(file1, file2, file3));
    when(auditLogService.findFile(anyString())).thenReturn(Optional.empty());
    when(auditLogService.findFile("file1")).thenReturn(Optional.of(file1));
    when(auditLogService.findFile("file2")).thenReturn(Optional.of(file2));
    when(auditLogService.findFile("file3")).thenReturn(Optional.of(file3));

    when(auditLogService.getLogs(anyString(), any())).thenReturn(Lists.newArrayList(
        new AuditLog(Instant.ofEpochMilli(1644687061000L), "message1", true),
        new AuditLog(Instant.ofEpochMilli(1644687062000L), "message2", true),
        new AuditLog(Instant.ofEpochMilli(1644687063000L), "message3", true),
        new AuditLog(Instant.ofEpochMilli(1644687064000L), "message4", true),
        new AuditLog(Instant.ofEpochMilli(1644687065000L), "message5", true)
    ));
  }

  @Test
  void shouldListLogFiles() throws Exception {
    mockMvc.perform(get("/audit-logs")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())

        .andExpect(jsonPath("_embedded.files", hasSize(3)))

        .andExpect(jsonPath("_embedded.files[0].filename", is("file1")))
        .andExpect(jsonPath("_embedded.files[0].modifiedAt", is(1644681000000L)))
        .andExpect(jsonPath("_embedded.files[0].size", is(10000)))

        .andExpect(jsonPath("_embedded.files[1].filename", is("file2")))
        .andExpect(jsonPath("_embedded.files[1].modifiedAt", is(1644682000000L)))
        .andExpect(jsonPath("_embedded.files[1].size", is(20000)))

        .andExpect(jsonPath("_embedded.files[2].filename", is("file3")))
        .andExpect(jsonPath("_embedded.files[2].modifiedAt", is(1644683000000L)))
        .andExpect(jsonPath("_embedded.files[2].size", is(30000)));
  }

  @Test
  void shouldReturn404ForFileDetails() throws Exception {
    mockMvc.perform(get("/audit-logs/fileXXX")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnFileDetails() throws Exception {
    mockMvc.perform(get("/audit-logs/file1")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())

        .andExpect(jsonPath("filename", is("file1")))
        .andExpect(jsonPath("size", is(10000)));
  }

  @Test
  void shouldDeleteFile() throws Exception {
    mockMvc.perform(delete("/audit-logs/file2")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful());

    verify(auditLogService).deleteFile("file2");
  }

  @Test
  void shouldReturnLogEntries() throws Exception {
    mockMvc.perform(get("/audit-logs/file1/entries")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())

        .andExpect(jsonPath("_embedded.entries", hasSize(5)))

        .andExpect(jsonPath("_embedded.entries[0].createdAt", is(1644687061000L)))
        .andExpect(jsonPath("_embedded.entries[0].message", is("message1")))

        .andExpect(jsonPath("_embedded.entries[1].createdAt", is(1644687062000L)))
        .andExpect(jsonPath("_embedded.entries[1].message", is("message2")))

        .andExpect(jsonPath("_embedded.entries[2].createdAt", is(1644687063000L)))
        .andExpect(jsonPath("_embedded.entries[2].message", is("message3")))

        .andExpect(jsonPath("_embedded.entries[3].createdAt", is(1644687064000L)))
        .andExpect(jsonPath("_embedded.entries[3].message", is("message4")))

        .andExpect(jsonPath("_embedded.entries[4].createdAt", is(1644687065000L)))
        .andExpect(jsonPath("_embedded.entries[4].message", is("message5")));
  }

}
