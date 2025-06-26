package org.nirvikalpa.multitenancy.datasource;

import org.junit.jupiter.api.Test;
import org.nirvikalpa.multitenancy.context.TenantContextHolder;
import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;
import org.nirvikalpa.multitenancy.registry.MultiTenantRegistry;
import org.nirvikalpa.multitenancy.registry.TenantDescriptor;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TenantRoutingDataSourceTest {

    @Test
    void shouldCreateAndCacheDatasourcePerTenant() throws SQLException {
        MultiTenantRegistry registry = mock(MultiTenantRegistry.class);
        TenantDescriptor acmeTenant = new TenantDescriptor("acme", "jdbc:h2:mem:acme", "sa", "", null);

        when(registry.findByTenantId("acme")).thenReturn(Optional.of(acmeTenant));

        MultiTenancyProperties props = new MultiTenancyProperties();
        MultiTenancyProperties.Isolation.DatasourceTemplate template = new MultiTenancyProperties.Isolation.DatasourceTemplate();
        template.setDriverClassName("org.h2.Driver");
        template.setMaxPoolSize(5);
        template.setMinIdle(1); // ← Add this
        template.setIdleTimeout(300_000L);
        template.setMaxLifetime(600_000L);
        props.getIsolation().setDatasourceTemplate(template);

        TenantContextHolder.setTenantId("acme");

        TenantRoutingDataSource routingDataSource = new TenantRoutingDataSource(registry, props);
        routingDataSource.setTargetDataSources(new HashMap<>());
        routingDataSource.afterPropertiesSet();

        DataSource ds = routingDataSource.determineTargetDataSource();
        assertNotNull(ds.getConnection()); // connect test
    }
}
