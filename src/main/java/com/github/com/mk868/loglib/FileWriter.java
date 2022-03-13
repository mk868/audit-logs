package com.github.com.mk868.loglib;

import com.github.com.mk868.loglib.model.FileEntry;
import com.github.com.mk868.loglib.serializer.EntrySerializer;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileWriter implements Closeable {

  private static final Logger log = LoggerFactory.getLogger(FileWriter.class);
  private static final String LINE_SEPARATOR = "\n";

  private final ExecutorService executorService;
  private final EntrySerializer serializer;
  private boolean started;
  private OutputStream fos;

  public FileWriter(File file) throws IOException {
    this.executorService = Executors.newSingleThreadExecutor();
    this.serializer = new EntrySerializer();

    fos = Files.newOutputStream(file.toPath());
    started = true;
  }

  /**
   * Put log on the writer queue
   *
   * @param fileEntry log entry to serialize and write
   */
  public void append(FileEntry<?> fileEntry) {
    if (!started) {
      log.warn("Cannot append log, stopped");
      return;
    }
    executorService.submit(this.createAppendTask(fileEntry));
  }

  /**
   * Parse and save entry to file
   *
   * @param fileEntry log entry to save
   */
  private void process(FileEntry<?> fileEntry) {
    var json =serializer.serialize(fileEntry);
    json += LINE_SEPARATOR;
    try {
      fos.write(json.getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      log.error("cannot write to file", e);
    }
  }

  /**
   * Create runnable object
   *
   * @param fileEntry log entry to save
   * @return runnable object
   */
  private Runnable createAppendTask(FileEntry<?> fileEntry) {
    return () -> this.process(fileEntry);
  }

  /**
   * Create task to close Stream
   *
   * @return runnable object
   */
  private Runnable createCloseTask() {
    return () -> {
      try {
        fos.flush();
        fos.close();
        fos = null;
      } catch (IOException e) {
        log.error("Cannot close stream");
      }
    };
  }

  @Override
  public void close() {
    started = false;
    executorService.submit(createCloseTask());
  }
}
