package org.nirvikalpa.multitenancy.context;

/**
 * Static holder for accessing the current TenantContext globally.
 * The context must be explicitly initialized externally by the framework or user.
 */
public class TenantContextHolder {
    private static TenantContext context;

    public static String getTenant() {
        ensureInitialized();
        return context.getCurrentTenant();
    }

    public static void setTenant(String tenantId) {
        ensureInitialized();
        context.setCurrentTenant(tenantId);
    }

    public static void clear() {
        if (context != null) {
            context.clear();
        }
    }

    public static void setContext(TenantContext customContext) {
        context = customContext;
    }

    private static void ensureInitialized() {
        if (context == null) {
            throw new IllegalStateException("TenantContext not initialized. Please set it using TenantContextHolder.setContext().");
        }
    }
}
