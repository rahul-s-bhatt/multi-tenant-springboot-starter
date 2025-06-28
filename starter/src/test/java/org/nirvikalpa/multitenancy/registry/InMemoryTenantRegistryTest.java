package org.nirvikalpa.multitenancy.registry;

import org.junit.jupiter.api.Test;
import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTenantRegistryTest {

    @Test
    void shouldResolveTenantFromYamlMap() {
        MultiTenancyProperties properties = new MultiTenancyProperties();
        MultiTenancyProperties.Registry registry = new MultiTenancyProperties.Registry();
        List<MultiTenancyProperties.Registry.InMemoryTenant> tenants = List.of(
                new MultiTenancyProperties.Registry.InMemoryTenant("acme", "jdbc:h2:mem:acme", "sa", ""),
                new MultiTenancyProperties.Registry.InMemoryTenant("globex", "jdbc:h2:mem:globex", "sa", "")
        );
        registry.setInMemoryTenants(tenants);
        properties.setRegistry(registry);
        InMemoryTenantRegistry inMemoryTenantRegistry = new InMemoryTenantRegistry(properties);
        assertEquals(2, inMemoryTenantRegistry.getAllTenants().size());

        TenantDescriptor tenant = inMemoryTenantRegistry.findByTenantId("acme").orElseThrow();
        assertEquals("jdbc:h2:mem:acme", tenant.datasourceUrl());
    }
}
