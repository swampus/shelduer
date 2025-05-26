package io.github.swampus;

/**
 * Static access controller interface.
 */
public class ShelduerEngine {
    private static final LedgerTable ledger = new LedgerTable();

    public static void allowAccess(String key) {
        ledger.allow(key);
    }

    public static void blockAccess(String key) {
        ledger.block(key);
    }

    public static boolean isAccessAllowed(String key) {
        return ledger.isAllowed(key);
    }

    public static void reset() {
        ledger.revokeAll();
    }
}
