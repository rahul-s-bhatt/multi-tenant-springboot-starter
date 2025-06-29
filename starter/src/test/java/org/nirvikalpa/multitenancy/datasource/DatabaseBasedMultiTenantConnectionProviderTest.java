package org.nirvikalpa.multitenancy.datasource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;
import org.nirvikalpa.multitenancy.registry.MultiTenantRegistry;
import org.nirvikalpa.multitenancy.registry.TenantDescriptor;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DatabaseBasedMultiTenantConnectionProviderTest {

    private MultiTenantRegistry registry;
    private MultiTenancyProperties props;

    @BeforeEach
    void setup() {
        registry = mock(MultiTenantRegistry.class);
        props = new MultiTenancyProperties();
        props.setDefaultTenantId("tenantA");
        props.getIsolation().getDatasourceTemplate().setDriverClassName("org.h2.Driver");
    }

    @Test
    void shouldProvideConnectionFromTenantDatasource() throws SQLException {
        var descriptor = new TenantDescriptor("tenantA", "jdbc:h2:mem:test", "sa", "", null);
        when(registry.findByTenantId("tenantA")).thenReturn(Optional.of(descriptor));

        var provider = new DatabaseBasedMultiTenantConnectionProvider(registry, props);
        var conn = provider.getConnection("tenantA");

        assertNotNull(conn);
        conn.close();
    }
}
