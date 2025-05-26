package io.github.swampus;

import java.lang.annotation.*;

/**
 * Annotation to mark methods or fields that require runtime access control.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface AccessControlled {
    String value(); // e.g., "ai.retrain"
}