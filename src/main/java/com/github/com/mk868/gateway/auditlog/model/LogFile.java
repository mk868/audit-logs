package com.github.com.mk868.gateway.auditlog.model;

import java.time.Instant;

public class LogFile {

  private String filename;
  private Instant modifiedAt;
  private long size;

  public LogFile() {
  }

  public LogFile(String filename, Instant modifiedAt, long size) {
    this.filename = filename;
    this.modifiedAt = modifiedAt;
    this.size = size;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public Instant getModifiedAt() {
    return modifiedAt;
  }

  public void setModifiedAt(Instant modifiedAt) {
    this.modifiedAt = modifiedAt;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }
}
