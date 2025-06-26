package org.nirvikalpa.multitenancy.context;

/**
 * Static holder for accessing the current TenantContext globally.
 * The context must be explicitly initialized externally by the framework or user.
 */
public class TenantContextHolder {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static String getTenantId() {
        return CURRENT_TENANT.get();
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}