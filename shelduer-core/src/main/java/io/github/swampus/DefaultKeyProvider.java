package io.github.swampus;

import io.github.swampus.KeyProvider;

import java.lang.reflect.Method;

/**
 * Default key provider returning a static key.
 */
public class DefaultKeyProvider implements KeyProvider {

    @Override
    public String resolveKey(Object target, Method method, Object[] args) {
        return "default-key";
    }

}