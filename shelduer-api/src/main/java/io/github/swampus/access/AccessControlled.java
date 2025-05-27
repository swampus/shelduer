package io.github.swampus.access;

import io.github.swampus.KeyProvider;
import io.github.swampus.policy.AccessPolicy;

import java.lang.annotation.*;

/**
 * Annotation to mark methods or fields that require runtime access control.
 */
// Updated annotation with additional parameters
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface AccessControlled {

    /**
     * Static resource name for locking. Optional if key or keyProvider is set.
     */
    String value() default "";

    /**
     * Optional dynamic key using expression language (SpEL or custom).
     */
    String key() default "";

    /**
     * Optional custom key provider class.
     */
    Class<? extends KeyProvider> keyProvider() default DefaultKeyProvider.class;

    /**
     * Access policy for scheduling.
     */
    AccessPolicy policy() default AccessPolicy.EXCLUSIVE;

    /**
     * Optional timeout in milliseconds.
     */
    long timeoutMs() default 0;
}