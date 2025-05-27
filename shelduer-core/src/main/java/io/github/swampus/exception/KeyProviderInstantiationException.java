package io.github.swampus.exception;

/**
 * Thrown when a KeyProvider cannot be instantiated or invoked.
 */
public class KeyProviderInstantiationException extends RuntimeException {

    public KeyProviderInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeyProviderInstantiationException(String message) {
        super(message);
    }
}
