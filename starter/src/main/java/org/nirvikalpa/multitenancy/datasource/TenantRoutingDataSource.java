package org.nirvikalpa.multitenancy.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.nirvikalpa.multitenancy.context.TenantContextHolder;
import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;
import org.nirvikalpa.multitenancy.registry.MultiTenantRegistry;
import org.nirvikalpa.multitenancy.registry.TenantDescriptor;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TenantRoutingDataSource extends AbstractRoutingDataSource {

    private final MultiTenantRegistry tenantRegistry;
    private final Map<String, DataSource> resolvedDataSources = new ConcurrentHashMap<>();
    private final MultiTenancyProperties.Isolation.DatasourceTemplate template;

    public TenantRoutingDataSource(MultiTenantRegistry tenantRegistry, MultiTenancyProperties props) {
        this.tenantRegistry = tenantRegistry;
        this.template = props.getIsolation().getDatasourceTemplate();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContextHolder.getTenantId();
    }

    @Override
    protected DataSource determineTargetDataSource() {
        String tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new IllegalStateException("No tenant ID set in context");

        return resolvedDataSources.computeIfAbsent(tenantId, id -> {
            TenantDescriptor tenant = tenantRegistry.findByTenantId(id)
                    .orElseThrow(() -> new IllegalStateException("Tenant not found: " + id));
            return createDataSource(tenant);
        });
    }

    private DataSource createDataSource(TenantDescriptor tenant) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(tenant.datasourceUrl());
        config.setUsername(tenant.username());
        config.setPassword(tenant.password());
        config.setDriverClassName(template.getDriverClassName());
        config.setMaximumPoolSize(template.getMaxPoolSize());
        config.setMinimumIdle(template.getMinIdle());
        config.setIdleTimeout(template.getIdleTimeout());
        config.setMaxLifetime(template.getMaxLifetime());
        return new HikariDataSource(config);
    }
}
