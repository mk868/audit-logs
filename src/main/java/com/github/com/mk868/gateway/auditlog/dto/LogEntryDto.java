package com.github.com.mk868.gateway.auditlog.dto;

import java.util.Objects;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "entries")
public class LogEntryDto {

  private long createdAt;
  private String message;

  public long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(long createdAt) {
    this.createdAt = createdAt;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LogEntryDto that = (LogEntryDto) o;
    return createdAt == that.createdAt && Objects.equals(message, that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(createdAt, message);
  }
}
