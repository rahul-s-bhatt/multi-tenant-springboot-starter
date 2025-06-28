package org.nirvikalpa.multitenancy.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.nirvikalpa.multitenancy.actuators.MultiTenancyStatusEndpoint;
import org.nirvikalpa.multitenancy.applicationlistener.MultiTenancyStartupListener;
import org.nirvikalpa.multitenancy.datasource.TenantRoutingDataSource;
import org.nirvikalpa.multitenancy.pojo.MultiTenancyDiagnosticsReporter;
import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;
import org.nirvikalpa.multitenancy.registry.InMemoryTenantRegistry;
import org.nirvikalpa.multitenancy.registry.JdbcTenantRegistry;
import org.nirvikalpa.multitenancy.registry.MultiTenantRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
public class MultitenancyAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "multi-tenancy.registry.type", havingValue = "IN_MEMORY")
    public MultiTenantRegistry inMemoryRegistry(MultiTenancyProperties props) {
        return new InMemoryTenantRegistry(props.getRegistry().getInMemoryTenants());
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

}
