package org.nirvikalpa.multitenancy.datasource;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.nirvikalpa.multitenancy.exceptions.MissingTenantException;
import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;
import org.nirvikalpa.multitenancy.registry.MultiTenantRegistry;
import org.nirvikalpa.multitenancy.registry.TenantDescriptor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DatabaseBasedMultiTenantConnectionProvider implements MultiTenantConnectionProvider<String> {

    private final MultiTenantRegistry registry;
    private final MultiTenancyProperties.Isolation.DatasourceTemplate template;
    private final String defaultTenantId;
    private final Map<String, DataSource> resolvedDataSources = new ConcurrentHashMap<>();

    public DatabaseBasedMultiTenantConnectionProvider(
            MultiTenantRegistry registry,
            MultiTenancyProperties props
    ) {
        this.registry = registry;
        this.template = props.getIsolation().getDatasourceTemplate();
        this.defaultTenantId = props.getDefaultTenantId();
    }

    private DataSource resolveDataSource(String tenantId) {
        return resolvedDataSources.computeIfAbsent(tenantId, id -> {
            TenantDescriptor descriptor = registry.findByTenantId(id)
                    .orElseThrow(() -> new MissingTenantException("No tenant config found for " + id));
            return descriptor.buildDataSourceFromTemplate(template);
        });
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return resolveDataSource(defaultTenantId).getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        return resolveDataSource(tenantIdentifier).getConnection();
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return MultiTenantConnectionProvider.class.equals(unwrapType) ||
                DatabaseBasedMultiTenantConnectionProvider.class.isAssignableFrom(unwrapType);
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        if (isUnwrappableAs(unwrapType)) {
            return unwrapType.cast(this);
        }
        throw new IllegalArgumentException("Unknown unwrap type: " + unwrapType.getName());
    }
}