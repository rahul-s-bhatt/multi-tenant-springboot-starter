package org.nirvikalpa.multitenancy.datasource;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.nirvikalpa.multitenancy.exceptions.MissingTenantException;
import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;
import org.nirvikalpa.multitenancy.registry.MultiTenantRegistry;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class SchemaBasedMultiTenantConnectionProvider implements MultiTenantConnectionProvider<String> {

    private final MultiTenantRegistry registry;
    private final MultiTenancyProperties.Isolation.DatasourceTemplate template;
    private final String defaultTenantId;
    private final DataSource sharedDataSource;

    public SchemaBasedMultiTenantConnectionProvider(
            MultiTenantRegistry registry,
            MultiTenancyProperties properties
    ) {
        this.registry = registry;
        this.template = properties.getIsolation().getDatasourceTemplate();
        this.defaultTenantId = properties.getDefaultTenantId();

        // All tenants share this DataSource (created once)
        this.sharedDataSource = registry.findByTenantId(defaultTenantId)
                .orElseThrow(() -> new MissingTenantException("No default tenant found"))
                .buildDataSourceFromTemplate(template);
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return sharedDataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = sharedDataSource.getConnection();

        // Switch schema: DB-agnostic logic (basic support)
        if (template.getDriverClassName().contains("postgresql")) {
            connection.createStatement().execute("SET SCHEMA '" + tenantIdentifier + "'");
        } else if (template.getDriverClassName().contains("oracle")) {
            connection.createStatement().execute("ALTER SESSION SET CURRENT_SCHEMA = " + tenantIdentifier);
        } else {
            throw new UnsupportedOperationException("Schema switching not implemented for driver: " + template.getDriverClassName());
        }

        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        // Reset to default schema if needed
        connection.createStatement().execute("SET SCHEMA '" + defaultTenantId + "'");
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
        return MultiTenantConnectionProvider.class.equals(unwrapType)
                || SchemaBasedMultiTenantConnectionProvider.class.isAssignableFrom(unwrapType);
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        if (isUnwrappableAs(unwrapType)) {
            return unwrapType.cast(this);
        }
        throw new IllegalArgumentException("Unknown unwrap type: " + unwrapType.getName());
    }
}
