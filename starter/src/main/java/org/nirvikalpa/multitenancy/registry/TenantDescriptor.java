package org.nirvikalpa.multitenancy.registry;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;

import javax.sql.DataSource;
import java.util.Optional;

public record TenantDescriptor(
        String tenantId,
        String datasourceUrl,
        String username,
        String password,
        String schemaName // nullable for per-db strategy
) {
    public DataSource buildDataSourceFromTemplate(MultiTenancyProperties.Isolation.DatasourceTemplate template) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(this.datasourceUrl());
        config.setUsername(this.username());
        config.setPassword(this.password());
        config.setDriverClassName(template.getDriverClassName());
        config.setMaximumPoolSize(Optional.ofNullable(template.getMaxPoolSize()).orElse(10));
        config.setMinimumIdle(Optional.ofNullable(template.getMinIdle()).orElse(1));
        config.setIdleTimeout(Optional.ofNullable(template.getIdleTimeout()).orElse(300_000L));
        config.setMaxLifetime(Optional.ofNullable(template.getMaxLifetime()).orElse(600_000L));
        return new HikariDataSource(config);
    }
}
