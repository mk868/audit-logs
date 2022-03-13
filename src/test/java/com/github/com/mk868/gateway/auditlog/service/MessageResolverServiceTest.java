package com.github.com.mk868.gateway.auditlog.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MessageResolverService.class)
class MessageResolverServiceTest {

  static final String TEST_CODE1 = "TEST_CODE1";
  static final String TEST_CODE2 = "TEST_CODE2";
  static final String TEST_CODE3 = "TEST_CODE3";
  static final String TEST_CODE4 = "TEST_CODE4";

  @Autowired
  MessageResolverService resolver;

  @Test
  void shouldReturnShortMessage() {
    var message = resolver.getMessage(TEST_CODE1, new HashMap<>(), Locale.ENGLISH);

    assertThat(message)
        .isPresent()
        .get()
        .isEqualTo("short message1");
  }

  @Test
  void shouldReturnMessageForDefaultLocale() {
    // there is no audit-cn.properties file
    var message = resolver.getMessage(TEST_CODE1, new HashMap<>(), Locale.CHINA);

    assertThat(message)
        .isPresent()
        .get()
        .isEqualTo("short message1");
  }

  @Test
  void shouldReturnMessageForDefaultLocale2() {
    // there is no "TEST_CODE1" key in the audit_de.properties file
    var message = resolver.getMessage(TEST_CODE1, new HashMap<>(), Locale.GERMANY);

    assertThat(message)
        .isPresent()
        .get()
        .isEqualTo("short message1");
  }

  @Test
  void shouldReturnMessageForNonDefaultLocale() {
    var message = resolver.getMessage(TEST_CODE2, new HashMap<>(), Locale.GERMANY);

    assertThat(message)
        .isPresent()
        .get()
        .isEqualTo("DE short message2");
  }

  @Test
  void shouldIgnoreMissingParams() {
    Map<String, Object> params = Map.of(
        "bbb", "222"
    );

    // we're not providing value for 'aaa' param
    var message = resolver.getMessage(TEST_CODE3, params, Locale.ENGLISH);

    assertThat(message)
        .isPresent()
        .get()
        .isEqualTo("message with params ${aaa}");
  }

  @Test
  void shouldPutParamsInMessage() {
    Map<String, Object> params = Map.of(
        "aaa", "111",
        "bbb", "222",
        "ccc", "333"
    );

    var message = resolver.getMessage(TEST_CODE4, params, Locale.ENGLISH);

    assertThat(message)
        .isPresent()
        .get()
        .isEqualTo("message with params 111, 222, 333");
  }

  @Test
  void shouldReturnEmptyWhenNoCodeMissing() {
    var message = resolver.getMessage("NON_EXISTING_CODE", new HashMap<>(), Locale.ENGLISH);

    assertThat(message)
        .isNotPresent();
  }

}
