package org.nirvikalpa.multitenancy.registry;

import java.util.List;
import java.util.Optional;

/**
 * Contract for tenant metadata lookup.
 */
public interface MultiTenantRegistry {

    List<TenantDescriptor> getAllTenants();

    Optional<TenantDescriptor> findByTenantId(String tenantId);

    default void refresh() {
        // Optional override
    }
}
