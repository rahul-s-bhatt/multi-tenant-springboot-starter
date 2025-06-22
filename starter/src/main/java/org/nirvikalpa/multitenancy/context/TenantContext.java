package org.nirvikalpa.multitenancy.context;

/**
 * Contract to manage the tenant context during a request lifecycle.
 */
public interface TenantContext {
    String getCurrentTenant();
    void setCurrentTenant(String tenantId);
    void clear();
}