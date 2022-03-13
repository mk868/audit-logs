package com.github.com.mk868.gateway.config;

import com.github.com.mk868.gateway.auditlog.service.AuditLogService;
import com.github.com.mk868.loglib.FileWriter;
import java.io.File;
import java.io.IOException;
import javax.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogLibConfig {

  private FileWriter fileWriter;

  @Bean
  public FileWriter fileWriter() throws IOException {
    return new FileWriter(new File("temp" + AuditLogService.LOG_EXTENSION));
  }

  @PreDestroy
  public void fileWriterStop() {
    fileWriter.close();
  }

}
