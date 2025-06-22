package org.nirvikalpa.multitenancy.context;


/**
 * Default thread-local implementation of the TenantContext.
 */
public class DefaultTenantContext implements TenantContext {
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    @Override
    public String getCurrentTenant() {
        return CONTEXT.get();
    }

    @Override
    public void setCurrentTenant(String tenantId) {
        CONTEXT.set(tenantId);
    }

    @Override
    public void clear() {
        CONTEXT.remove();
    }
}