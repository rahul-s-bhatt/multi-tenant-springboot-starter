package org.nirvikalpa.multitenancy.datasource;

import org.nirvikalpa.multitenancy.context.TenantContextHolder;
import org.nirvikalpa.multitenancy.registry.TenantDefinition;
import org.nirvikalpa.multitenancy.registry.TenantRegistry;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DataSource router that delegates to the correct tenant-specific DataSource
 * based on the current tenant context.
 */
public class TenantRoutingDataSource extends AbstractRoutingDataSource {

    private final TenantRegistry tenantRegistry;
    private final Map<String, DataSource> resolvedDataSources = new ConcurrentHashMap<>();
    private final DataSourceFactory dataSourceFactory;

    public TenantRoutingDataSource(TenantRegistry tenantRegistry, DataSourceFactory dataSourceFactory) {
        this.tenantRegistry = tenantRegistry;
        this.dataSourceFactory = dataSourceFactory;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContextHolder.getTenant();
    }

    @Override
    protected DataSource determineTargetDataSource() {
        String tenantId = (String) determineCurrentLookupKey();
        return resolvedDataSources.computeIfAbsent(tenantId, this::resolveDataSourceForTenant);
    }

    private DataSource resolveDataSourceForTenant(String tenantId) {
        return tenantRegistry.findTenant(tenantId)
                .map(dataSourceFactory::createDataSource)
                .orElseThrow(() -> new IllegalStateException("No tenant definition found for tenant: " + tenantId));
    }
}
