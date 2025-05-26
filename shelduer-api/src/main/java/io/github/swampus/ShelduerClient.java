package io.github.swampus;

/**
 * External API for allowing/blocking runtime access to resources.
 * Can be used by AI systems or dynamic module loaders.
 */
public class ShelduerClient {

    public static void allow(String key) {
        ShelduerEngine.allowAccess(key);
    }

    public static void block(String key) {
        ShelduerEngine.blockAccess(key);
    }

    public static boolean isAllowed(String key) {
        return ShelduerEngine.isAccessAllowed(key);
    }

    public static void resetAll() {
        ShelduerEngine.reset();
    }
}