package io.github.swampus.core.access;

/**
 * Interface for access control lookup.
 */
public interface IAccessManager {
    boolean isAccessAllowed(String key);
}
