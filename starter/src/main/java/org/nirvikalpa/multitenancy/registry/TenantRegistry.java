package org.nirvikalpa.multitenancy.registry;

import java.util.Map;
import java.util.Optional;

/**
 * Contract for tenant metadata lookup.
 */
public interface TenantRegistry {

    /**
     * Find tenant metadata by tenant ID.
     * @param tenantId the identifier of the tenant
     * @return optional tenant definition
     */
    Optional<TenantDefinition> findTenant(String tenantId);

    /**
     * List all known tenants.
     * @return map of tenant ID to tenant definitions
     */
    Map<String, TenantDefinition> findAllTenants();
}
