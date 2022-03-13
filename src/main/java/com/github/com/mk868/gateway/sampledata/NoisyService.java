package com.github.com.mk868.gateway.sampledata;

import com.github.com.mk868.gateway.auditlog.AuditLogger;
import com.github.com.mk868.gateway.auditlog.log.DummyLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class NoisyService {

  private static final Logger log = LoggerFactory.getLogger(NoisyService.class);

  private final AuditLogger auditLogger;

  public NoisyService(AuditLogger auditLogger) {
    this.auditLogger = auditLogger;
  }

  @Scheduled(fixedDelay = 1000)
  public void scheduleFixedDelayTask() {
    var auditLog = new DummyLog("string ABC", (int) ((System.currentTimeMillis() / 1000) % 1000),
        false);
    log.info("Adding log {}", auditLog);
    auditLogger.log(auditLog);

  }

}
