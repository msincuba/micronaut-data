package io.micronaut.data.processor.visitors.finders;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.data.processor.visitors.MatchContext;
import io.micronaut.inject.ast.ClassElement;
import io.micronaut.inject.ast.MethodElement;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Simple list method support.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public class ListMethod extends AbstractListMethod {

    /**
     * The prefixes used.
     */
    public static final String[] PREFIXES = {"list", "find", "search", "query"};

    /**
     * Default constructor.
     */
    public ListMethod() {
        super(
                PREFIXES
        );
    }

    @Override
    public int getOrder() {
        // lower priority than dynamic finder
        return DEFAULT_POSITION + 100;
    }

    @Override
    public boolean isMethodMatch(@NonNull MethodElement methodElement, @NonNull MatchContext matchContext) {
        return super.isMethodMatch(methodElement, matchContext)
                && isValidReturnType(matchContext.getReturnType(), matchContext);

    }

    /**
     * Dictates whether this is a valid return type.
     * @param returnType The return type.
     * @param matchContext The match context
     * @return True if it is
     */
    protected boolean isValidReturnType(@NonNull ClassElement returnType, MatchContext matchContext) {
        return returnType.isAssignable(Iterable.class);
    }

}
