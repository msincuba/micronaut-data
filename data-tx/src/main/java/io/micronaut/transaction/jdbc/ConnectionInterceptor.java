package io.micronaut.transaction.jdbc;

import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.context.BeanContext;
import io.micronaut.context.Qualifier;
import io.micronaut.core.annotation.Internal;
import io.micronaut.transaction.exceptions.NoTransactionException;
import io.micronaut.transaction.jdbc.exceptions.CannotGetJdbcConnectionException;

import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;

/**
 * An interceptor that allows injecting a {@link Connection} that acts a proxy to lookup the connection for the current transaction.
 *
 * @author graemerocher
 * @since 1.0
 */
@Singleton
public final class ConnectionInterceptor implements MethodInterceptor<Connection, Object> {

    private final DataSource dataSource;

    /**
     * Default constructor.
     * @param beanContext The bean context
     * @param qualifier The qualifier
     */
    @Internal
    ConnectionInterceptor(BeanContext beanContext, Qualifier qualifier) {
        this.dataSource = beanContext.getBean(DataSource.class, qualifier);
    }

    @Override
    public Object intercept(MethodInvocationContext<Connection, Object> context) {
        Connection connection;
        try {
            connection = DataSourceUtils.getConnection(dataSource, false);
        } catch (CannotGetJdbcConnectionException e) {
            throw new NoTransactionException("No current transaction present. Consider declaring @Transactional on the surrounding method");
        }
        return context.getExecutableMethod().invoke(connection, context.getParameterValues());
    }
}
