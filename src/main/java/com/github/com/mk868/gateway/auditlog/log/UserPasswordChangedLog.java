package com.github.com.mk868.gateway.auditlog.log;

@AuditLogId("USER_PASSWORD_CHANGED")
public record UserPasswordChangedLog(String username) {

}
