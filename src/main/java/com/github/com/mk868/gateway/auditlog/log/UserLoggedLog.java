package com.github.com.mk868.gateway.auditlog.log;

@AuditLogId("USER_LOGGED")
public record UserLoggedLog(String username, String sessionId) {

}
