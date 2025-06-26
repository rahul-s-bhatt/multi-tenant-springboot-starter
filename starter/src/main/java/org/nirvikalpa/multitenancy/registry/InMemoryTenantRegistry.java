package org.nirvikalpa.multitenancy.registry;


import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Simple in-memory tenant registry used for local development and testing.
 */
public class InMemoryTenantRegistry implements MultiTenantRegistry {

    private final Map<String, TenantDescriptor> tenantMap;

    public InMemoryTenantRegistry(Map<String, MultiTenancyProperties.Registry.InMemoryTenant> inMemoryConfig) {
        this.tenantMap = inMemoryConfig.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            MultiTenancyProperties.Registry.InMemoryTenant tenant = entry.getValue();
                            return new TenantDescriptor(
                                    tenant.getTenantId(),
                                    tenant.getDatasourceUrl(),
                                    tenant.getUsername(),
                                    tenant.getPassword(),
                                    null // schemaName is optional
                            );
                        }
                ));
    }

    @Override
    public List<TenantDescriptor> getAllTenants() {
        return new ArrayList<>(tenantMap.values());
    }

    @Override
    public Optional<TenantDescriptor> findByTenantId(String tenantId) {
        return Optional.ofNullable(tenantMap.get(tenantId));
    }
}
