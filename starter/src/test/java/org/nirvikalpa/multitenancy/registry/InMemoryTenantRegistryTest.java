package org.nirvikalpa.multitenancy.registry;

import org.junit.jupiter.api.Test;
import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTenantRegistryTest {

    @Test
    void shouldResolveTenantFromYamlMap() {
        Map<String, MultiTenancyProperties.Registry.InMemoryTenant> tenants = Map.of(
                "acme", new MultiTenancyProperties.Registry.InMemoryTenant("acme", "jdbc:h2:mem:acme", "sa", ""),
                "globex", new MultiTenancyProperties.Registry.InMemoryTenant("globex", "jdbc:h2:mem:globex", "sa", "")
        );

        InMemoryTenantRegistry registry = new InMemoryTenantRegistry(tenants);
        assertEquals(2, registry.getAllTenants().size());

        TenantDescriptor tenant = registry.findByTenantId("acme").orElseThrow();
        assertEquals("jdbc:h2:mem:acme", tenant.datasourceUrl());
    }
}
