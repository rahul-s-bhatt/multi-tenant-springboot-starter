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

    public InMemoryTenantRegistry(MultiTenancyProperties props) {
        var inMemoryConfig = props.getRegistry().getInMemoryTenants();
        this.tenantMap = inMemoryConfig.stream()
                .collect(Collectors.toMap(
                        MultiTenancyProperties.Registry.InMemoryTenant::getTenantId,
                        entry -> {
                            return new TenantDescriptor(
                                    entry.getTenantId(),
                                    entry.getDatasourceUrl(),
                                    entry.getUsername(),
                                    entry.getPassword(),
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
