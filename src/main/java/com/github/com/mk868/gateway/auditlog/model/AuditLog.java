package com.github.com.mk868.gateway.auditlog.model;

import java.time.Instant;

public class AuditLog {

  private Instant createdAt;
  private String message;
  private boolean fallbackMessage;

  public AuditLog() {
  }

  public AuditLog(Instant createdAt, String message, boolean fallbackMessage) {
    this.createdAt = createdAt;
    this.message = message;
    this.fallbackMessage = fallbackMessage;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public boolean isFallbackMessage() {
    return fallbackMessage;
  }

  public void setFallbackMessage(boolean fallbackMessage) {
    this.fallbackMessage = fallbackMessage;
  }
}
