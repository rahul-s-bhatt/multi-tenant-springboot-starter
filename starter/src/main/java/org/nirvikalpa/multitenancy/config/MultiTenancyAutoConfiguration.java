package org.nirvikalpa.multitenancy.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.nirvikalpa.multitenancy.actuators.MultiTenancyStatusEndpoint;
import org.nirvikalpa.multitenancy.applicationlistener.MultiTenancyStartupListener;
import org.nirvikalpa.multitenancy.datasource.DatabaseBasedMultiTenantConnectionProvider;
import org.nirvikalpa.multitenancy.datasource.SchemaBasedMultiTenantConnectionProvider;
import org.nirvikalpa.multitenancy.datasource.TenantRoutingDataSource;
import org.nirvikalpa.multitenancy.enums.MultiTenantDataIsolationStrategyEnum;
import org.nirvikalpa.multitenancy.pojo.MultiTenancyDiagnosticsReporter;
import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;
import org.nirvikalpa.multitenancy.registry.InMemoryTenantRegistry;
import org.nirvikalpa.multitenancy.registry.JdbcTenantRegistry;
import org.nirvikalpa.multitenancy.registry.MultiTenantRegistry;
import org.nirvikalpa.multitenancy.resolver.HibernateCurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.HashMap;


/**
 * Auto-configuration for multitenancy components.
 */
@AutoConfiguration
@EnableConfigurationProperties(MultiTenancyProperties.class)
public class MultiTenancyAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "multi-tenancy.registry.type", havingValue = "IN_MEMORY")
    public MultiTenantRegistry inMemoryRegistry(MultiTenancyProperties props) {
        return new InMemoryTenantRegistry(props);
    }

    @Bean
    @ConditionalOnProperty(name = "multi-tenancy.registry.type", havingValue = "JDBC")
    public MultiTenantRegistry jdbcRegistry(MultiTenancyProperties props) {
        MultiTenancyProperties.Registry.Jdbc jdbc = props.getRegistry().getJdbc();

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbc.getUrl());
        hikariConfig.setUsername(jdbc.getUsername());
        hikariConfig.setPassword(jdbc.getPassword());
        hikariConfig.setDriverClassName(jdbc.getDriverClassName());

        DataSource registryDs = new HikariDataSource(hikariConfig);
        return new JdbcTenantRegistry(registryDs);
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSource tenantRoutingDataSource(MultiTenantRegistry tenantRegistry, MultiTenancyProperties props) {
        TenantRoutingDataSource ds = new TenantRoutingDataSource(tenantRegistry, props);
        ds.setTargetDataSources(new HashMap<>());
        ds.afterPropertiesSet();
        return ds;
    }

    @Bean
    @ConditionalOnMissingBean
    public MultiTenancyDiagnosticsReporter diagnosticsReporter(MultiTenancyProperties props, Environment environment) {
        return new MultiTenancyDiagnosticsReporter(environment, props);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> diagnosticsConsoleStartupListener(MultiTenancyDiagnosticsReporter multiTenancyDiagnosticsReporter) {
        return new MultiTenancyStartupListener(multiTenancyDiagnosticsReporter);
    }

    @Bean
    @ConditionalOnProperty(name = "multi-tenancy.actuator-enabled", havingValue = "true", matchIfMissing = false)
    public MultiTenancyStatusEndpoint multiTenancyStatusEndpoint(MultiTenancyDiagnosticsReporter multiTenancyDiagnosticsReporter){
        return new MultiTenancyStatusEndpoint(multiTenancyDiagnosticsReporter);
    }

    @Bean
    public HibernatePropertiesCustomizer hibernateCustomizer(MultiTenancyProperties props) {
        return hibernateProps -> {
            hibernateProps.put("hibernate.multi_tenant", mapType(props.getIsolation().getType()));
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public CurrentTenantIdentifierResolver<String> tenantIdentifierResolver() {
        return new HibernateCurrentTenantIdentifierResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public MultiTenantConnectionProvider<String> multiTenantConnectionProvider(
            MultiTenantRegistry registry,
            MultiTenancyProperties properties,
            DataSource source
    ) {
        return switch (properties.getIsolation().getType()) {
            case TENANT_PER_DATABASE -> new DatabaseBasedMultiTenantConnectionProvider(registry, properties);
            case TENANT_PER_SCHEMA -> new SchemaBasedMultiTenantConnectionProvider(source);
        };
    }

    private String mapType(MultiTenantDataIsolationStrategyEnum type) {
        return switch (type) {
            case TENANT_PER_DATABASE -> "DATABASE";
            case TENANT_PER_SCHEMA -> "SCHEMA";
        };
    }
}
