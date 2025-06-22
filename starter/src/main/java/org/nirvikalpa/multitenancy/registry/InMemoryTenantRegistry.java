package org.nirvikalpa.multitenancy.registry;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Simple in-memory tenant registry used for local development and testing.
 */
public class InMemoryTenantRegistry implements TenantRegistry {

    private final Map<String, TenantDefinition> tenantMap = new HashMap<>();

    public InMemoryTenantRegistry(Map<String, TenantDefinition> preconfiguredTenants) {
        if (preconfiguredTenants != null) {
            tenantMap.putAll(preconfiguredTenants);
        }
    }

    @Override
    public Optional<TenantDefinition> findTenant(String tenantId) {
        return Optional.ofNullable(tenantMap.get(tenantId));
    }

    @Override
    public Map<String, TenantDefinition> findAllTenants() {
        return Collections.unmodifiableMap(tenantMap);
    }
}
