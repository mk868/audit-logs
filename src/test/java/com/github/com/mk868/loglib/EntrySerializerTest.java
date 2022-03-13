package com.github.com.mk868.loglib;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.com.mk868.loglib.serializer.EntrySerializer;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntrySerializerTest {

  EntrySerializer serializer;

  @BeforeEach
  void init() {
    serializer = new EntrySerializer();
  }

  @Test
  void shouldReturnEmptyForInvalidInput() {
    var json = "{\"logId\":\"TEST\",\"createdAt\":1647041530254,\"param";

    var result = serializer.deserialize(json);

    assertThat(result).isEmpty();
  }

  @Test
  void shouldReturnAnonymousLogEntry() {
    var json = "{\"logId\":\"TEST\",\"createdAt\":1647041529253,\"params\":{\"str\":\"string ABC\",\"number\":123,\"b\":false}}";

    var result = serializer.deserialize(json);

    assertThat(result).isPresent();
    assertThat(result.get().getLogId()).isEqualTo("TEST");
    assertThat(result.get().getCreatedAt()).isEqualTo(Instant.ofEpochMilli(1647041529253L));
    assertThat(result.get().getParams())
        .hasSize(3)
        .containsEntry("str", "string ABC")
        .containsEntry("number", "123")
        .containsEntry("b", "false");
  }

}
