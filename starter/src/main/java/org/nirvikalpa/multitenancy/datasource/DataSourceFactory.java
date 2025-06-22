package org.nirvikalpa.multitenancy.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.nirvikalpa.multitenancy.properties.HikariTenantPoolProperties;
import org.nirvikalpa.multitenancy.registry.TenantDefinition;

import javax.sql.DataSource;

/**
 * Factory to create a DataSource instance from a TenantDefinition.
 */
public class DataSourceFactory {

    private final HikariTenantPoolProperties poolProperties;

    public DataSourceFactory(HikariTenantPoolProperties poolProperties) {
        this.poolProperties = poolProperties;
    }

    public DataSource createDataSource(TenantDefinition definition) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(definition.datasourceUrl());
        config.setUsername(definition.username());
        config.setPassword(definition.password());
        config.setPoolName("tenant-" + definition.tenantId());

        // Apply global pool properties
        config.setMaximumPoolSize(poolProperties.getMaxPoolSize());
        config.setMinimumIdle(poolProperties.getMinIdle());
        config.setIdleTimeout(poolProperties.getIdleTimeout());
        config.setMaxLifetime(poolProperties.getMaxLifetime());

        return new HikariDataSource(config);
    }
}
