package com.github.com.mk868.gateway.auditlog.dto;

import java.util.Objects;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "files")
public class LogFileDto extends RepresentationModel<LogFileDto> {

  private String filename;
  private long modifiedAt;
  private long size;

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public long getModifiedAt() {
    return modifiedAt;
  }

  public void setModifiedAt(long modifiedAt) {
    this.modifiedAt = modifiedAt;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    LogFileDto that = (LogFileDto) o;
    return modifiedAt == that.modifiedAt && size == that.size
        && Objects.equals(filename, that.filename);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), filename, modifiedAt, size);
  }
}
