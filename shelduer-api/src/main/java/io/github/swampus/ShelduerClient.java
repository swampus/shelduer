package io.github.swampus;

import io.github.swampus.access.IAccessManager;

/**
 * External API for allowing/blocking runtime access to resources.
 * Can be used by AI systems or dynamic module loaders.
 */
public class ShelduerClient implements IAccessManager {

    @Override
    public boolean isAccessAllowed(String key) {
        return ShelduerEngine.isAccessAllowed(key);
    }

    public void allow(String key) {
        ShelduerEngine.allowAccess(key);
    }

    public void block(String key) {
        ShelduerEngine.blockAccess(key);
    }

    public void resetAll() {
        ShelduerEngine.reset();
    }
}