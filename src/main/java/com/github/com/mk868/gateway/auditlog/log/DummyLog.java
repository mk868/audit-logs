package com.github.com.mk868.gateway.auditlog.log;

/**
 * Just for tests
 */
@AuditLogId("DUMMY")
public record DummyLog(String str, int number, boolean b) {

}
