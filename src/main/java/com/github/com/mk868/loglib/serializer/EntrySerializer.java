package com.github.com.mk868.loglib.serializer;

import com.github.com.mk868.loglib.model.AnonymousFileEntry;
import com.github.com.mk868.loglib.model.FileEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntrySerializer {

  private static final Logger log = LoggerFactory.getLogger(EntrySerializer.class);

  private final Gson gson;

  public EntrySerializer() {
    this.gson = new GsonBuilder()
        .registerTypeAdapter(Instant.class, new InstantTypeConverter())
        .create();
  }

  public Optional<AnonymousFileEntry> deserialize(String json) {
    Objects.requireNonNull(json, "json must not be null");

    try {
      var value = gson.fromJson(json, AnonymousFileEntry.class);
      return Optional.of(value);
    } catch (JsonParseException ex) {
      log.warn("Cannot parse json", ex);
      return Optional.empty();
    }
  }

  public String serialize(FileEntry<?> fileEntry) {
    Objects.requireNonNull(fileEntry, "fileEntry must not be null");

    return gson.toJson(fileEntry);
  }
}
