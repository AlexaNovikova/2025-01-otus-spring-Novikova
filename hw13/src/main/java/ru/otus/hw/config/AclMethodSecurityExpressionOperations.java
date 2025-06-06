package ru.otus.hw.config;

import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;

public interface AclMethodSecurityExpressionOperations extends MethodSecurityExpressionOperations {

    boolean isAdministrator(Object targetId, Class<?> targetClass);

    boolean isAdministrator(Object target);

    boolean canRead(Object targetId, Class<?> targetClass);

    boolean canDelete(Object targetId, Class<?> targetClass);

}
