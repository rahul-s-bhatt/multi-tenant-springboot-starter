package org.nirvikalpa.multitenancy.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.nirvikalpa.multitenancy.applicationlistener.ApplicationLifecycleTracker;
import org.nirvikalpa.multitenancy.context.TenantContextHolder;
import org.nirvikalpa.multitenancy.exceptions.MissingTenantException;
import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;
import org.nirvikalpa.multitenancy.registry.MultiTenantRegistry;
import org.nirvikalpa.multitenancy.registry.TenantDescriptor;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class TenantRoutingDataSource extends AbstractRoutingDataSource {

    private final MultiTenantRegistry multiTenantRegistry;
    private final Map<String, DataSource> resolvedDataSources = new ConcurrentHashMap<>();
    private final MultiTenancyProperties.Isolation.DatasourceTemplate template;
    private final String defaultTenantId;

    public TenantRoutingDataSource(MultiTenantRegistry multiTenantRegistry, MultiTenancyProperties props) {
        this.multiTenantRegistry = multiTenantRegistry;
        this.template = props.getIsolation().getDatasourceTemplate();
        this.defaultTenantId = props.getDefaultTenantId();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContextHolder.getTenantId();
    }

    @Override
    protected DataSource determineTargetDataSource() {
        String tenantId = TenantContextHolder.getTenantId();

        if(!ApplicationLifecycleTracker.isApplicationStarted()) return resolveDefaultDataSource();
        if (tenantId == null) throw new MissingTenantException("No tenant ID set in context");

        return resolvedDataSources.computeIfAbsent(tenantId, id -> {
            TenantDescriptor tenant = multiTenantRegistry.findByTenantId(id)
                    .orElseThrow(() -> new MissingTenantException("Tenant not found: " + id));
            return createDataSource(tenant);
        });
    }

    private DataSource createDataSource(TenantDescriptor tenant) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(tenant.datasourceUrl());
        config.setUsername(tenant.username());
        config.setPassword(tenant.password());
        config.setDriverClassName(template.getDriverClassName());
        config.setMaximumPoolSize(Optional.ofNullable(template.getMaxPoolSize()).orElse(10));
        config.setMinimumIdle(Optional.ofNullable(template.getMinIdle()).orElse(1));
        config.setIdleTimeout(Optional.ofNullable(template.getIdleTimeout()).orElse(300_000L));
        config.setMaxLifetime(Optional.ofNullable(template.getMaxLifetime()).orElse(600_000L));
        return new HikariDataSource(config);
    }

    private DataSource resolveDefaultDataSource() {

        if(defaultTenantId == null) throw new MissingTenantException("Default tenant id not found in application.yml");

        return resolvedDataSources.computeIfAbsent(defaultTenantId, id -> {
            TenantDescriptor tenant = multiTenantRegistry.findByTenantId(id)
                    .orElseThrow(() -> new MissingTenantException("Default Tenant not found via registry: " + id));
            return createDataSource(tenant);
        });
    }
}
