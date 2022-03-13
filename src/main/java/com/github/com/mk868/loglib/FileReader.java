package com.github.com.mk868.loglib;

import com.github.com.mk868.loglib.model.AnonymousFileEntry;
import com.github.com.mk868.loglib.serializer.EntrySerializer;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileReader implements Closeable {

  private static final Logger log = LoggerFactory.getLogger(FileReader.class);

  private final EntrySerializer serializer;
  private LineNumberReader reader;

  public FileReader(File file) throws IOException {
    this.serializer = new EntrySerializer();
    this.reader = new LineNumberReader(Files.newBufferedReader(file.toPath()));
  }

  public List<AnonymousFileEntry> readAllEntries() throws IOException {
    var result = new ArrayList<AnonymousFileEntry>();
    String line;
    while ((line = reader.readLine()) != null) {
      serializer.deserialize(line).ifPresent(result::add);
    }
    return result;
  }

  @Override
  public void close() throws IOException {
    if (reader != null) {
      reader.close();
      reader = null;
    }
  }
}
