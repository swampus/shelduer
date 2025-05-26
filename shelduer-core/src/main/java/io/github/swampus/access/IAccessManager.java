package io.github.swampus.access;

/**
 * Interface for access control lookup.
 */
public interface IAccessManager {
    boolean isAccessAllowed(String key);
}
