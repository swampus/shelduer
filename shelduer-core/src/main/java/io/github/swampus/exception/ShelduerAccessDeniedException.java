package io.github.swampus.exception;

/**
 * Thrown when access to a method or field is denied by Shelduer.
 */
public class ShelduerAccessDeniedException extends RuntimeException {
    public ShelduerAccessDeniedException(String key) {
        super("Access to key [" + key + "] was denied by Shelduer.");
    }
}
