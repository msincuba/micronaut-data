package io.micronaut.transaction.jdbc;

import io.micronaut.aop.Introduction;
import io.micronaut.context.annotation.Type;
import io.micronaut.core.annotation.Internal;

import javax.inject.Scope;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * An introduction advice annotation used to create a transaction aware connection.
 * Considered internal and not for explicit usage.
 *
 * @see ConnectionInterceptor
 * @see InterceptedConnection
 */
@Retention(RUNTIME)
@Scope
@Introduction
@Type(ConnectionInterceptor.class)
@Internal
@interface ConnectionAdvice {
}
