package com.github.com.mk868.loglib.model;

import java.time.Instant;

/**
 * Model used to deserialize/serialize logs from/to JSON format.
 * <p>
 * Serialization strategy: create a record containing information about the log parameters, this
 * will avoid typo problems and make the code more readable.
 * <p>
 * {@snippet public record MyCustomLog(String param1, String param2) { } } Then you can create a new
 * FileEntry like that:{@snippet var entry = new FileEntry<MyCustomLog>(); entry.setLogId("MY_LOG");
 * entry.setParams(new MyCustomLog("value 1", "value 2")); }
 * <p>
 * Deserialization strategy: params type is Map<String, String> because we assume that log
 * parameters may have unknown names, so it would be impossible to map them to records existing in
 * the system.
 *
 * @param <T> params type, use record for serialization and {@code Map<String, String>} for
 *            deserialization
 * @see AnonymousFileEntry for deserialization
 */
public class FileEntry<T> {

  private String logId;
  private Instant createdAt;
  private T params;

  public String getLogId() {
    return logId;
  }

  public void setLogId(String logId) {
    this.logId = logId;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public T getParams() {
    return params;
  }

  public void setParams(T params) {
    this.params = params;
  }

}
