package com.github.com.mk868.gateway.auditlog.log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation used to tag each log record to give it a unique type id. This id is used to later find
 * the message template for internationalization.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditLogId {

  String value();
}
