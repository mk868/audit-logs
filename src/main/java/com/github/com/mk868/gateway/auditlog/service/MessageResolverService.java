package com.github.com.mk868.gateway.auditlog.service;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

@Service
public class MessageResolverService {

  private static final String BASENAME = "audit";

  private final MessageSource messageSource;

  public MessageResolverService() {
    var source = new ResourceBundleMessageSource();
    source.setBasenames(BASENAME);

    this.messageSource = source;
  }

  /**
   * Get message for specified log type
   *
   * @param code   log id
   * @param params log parameters
   * @param locale locale
   * @return log message for specified locale
   */
  public Optional<String> getMessage(String code, Map<String, Object> params, Locale locale) {
    var template = messageSource.getMessage(code, null, null, locale);

    if (template == null) {
      return Optional.empty();
    }

    if (params == null) {
      return Optional.of(template);
    }

    var sub = new StringSubstitutor(params);
    return Optional.of(sub.replace(template));
  }

}
