package io.github.swampus;

import java.lang.reflect.Method;

/**
 * Functional interface for providing dynamic access keys at runtime.
 */
@FunctionalInterface
public interface KeyProvider {

    /**
     * Resolves the access key for the given context.
     *
     * @param target the object on which the method is invoked
     * @param method the method being executed
     * @param args the method arguments
     * @return the resolved access key (e.g., user ID or resource name)
     */
    String resolveKey(Object target, Method method, Object[] args);
}
