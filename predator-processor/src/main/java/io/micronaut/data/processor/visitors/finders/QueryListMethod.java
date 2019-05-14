package io.micronaut.data.processor.visitors.finders;

import io.micronaut.core.util.StringUtils;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.intercept.FindAllInterceptor;
import io.micronaut.data.processor.visitors.MatchContext;
import io.micronaut.data.processor.visitors.MethodMatchContext;
import io.micronaut.inject.ast.MethodElement;
import io.micronaut.inject.ast.ParameterElement;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements support for explicit queries.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public class QueryListMethod extends ListMethod {

    public static final int POSITION = DEFAULT_POSITION - 200;

    private static final Pattern VARIABLE_PATTERN = Pattern.compile(":([a-zA-Z0-9]+)");

    @Override
    public int getOrder() {
        // give higher chance to match annotation
        return POSITION;
    }

    @Override
    public boolean isMethodMatch(MethodElement methodElement, MatchContext matchContext) {
        return methodElement.getValue(Query.class, String.class).map(StringUtils::isNotEmpty).orElse(false) && super.isMethodMatch(methodElement, matchContext);
    }

    @Nullable
    @Override
    public final MethodMatchInfo buildMatchInfo(@NonNull MethodMatchContext matchContext) {
        RawQuery query = buildRawQuery(matchContext);
        if (query == null) {
            return null;
        }
        return buildMatchInfo(matchContext, query);
    }

    protected MethodMatchInfo buildMatchInfo(@NonNull MethodMatchContext matchContext, @NonNull RawQuery query) {
        return new MethodMatchInfo(
                matchContext.getRootEntity(),
                query,
                FindAllInterceptor.class
        );
    }

    private RawQuery buildRawQuery(@NonNull MethodMatchContext matchContext) {
        MethodElement methodElement = matchContext.getMethodElement();
        String queryString = methodElement.getValue(Query.class, String.class).orElseThrow(() ->
            new IllegalStateException("Should only be called if Query has value!")
        );
        Matcher matcher = VARIABLE_PATTERN.matcher(queryString);
        List<ParameterElement> parameters = Arrays.asList(matchContext.getParameters());
        Map<String, String> parameterBinding = new LinkedHashMap<>(parameters.size());
        while (matcher.find()) {
            String name = matcher.group(1);
            Optional<ParameterElement> element = parameters.stream().filter(p -> p.getName().equals(name)).findFirst();
            if (element.isPresent()) {
                parameterBinding.put(name, element.get().getName());
            } else {
                matchContext.getVisitorContext().fail(
                        "No method parameter found for name :" + name,
                        methodElement
                );
                return null;
            }
        }

        return new RawQuery(matchContext.getRootEntity(), parameterBinding);
    }
}
