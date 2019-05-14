package io.micronaut.data.runtime.intercept;

import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.data.intercept.ExistsByInterceptor;
import io.micronaut.data.runtime.datastore.Datastore;

import edu.umd.cs.findbugs.annotations.NonNull;

public class DefaultExistsByInterceptor<T> extends AbstractQueryInterceptor<T, Boolean> implements ExistsByInterceptor<T> {
    DefaultExistsByInterceptor(@NonNull Datastore datastore) {
        super(datastore);
    }

    @Override
    public Boolean intercept(MethodInvocationContext<T, Boolean> context) {
        PreparedQuery preparedQuery = prepareQuery(context);
        Class idType = preparedQuery.getIdType();
        if (idType == null) {
            idType = preparedQuery.getRootEntity();
        }
        return datastore.findOne(
                idType,
                preparedQuery.getQuery(),
                preparedQuery.getParameterValues()
        ) != null;
    }
}
