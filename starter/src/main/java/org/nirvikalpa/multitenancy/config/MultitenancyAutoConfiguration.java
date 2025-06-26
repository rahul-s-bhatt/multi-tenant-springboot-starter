package org.nirvikalpa.multitenancy.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.nirvikalpa.multitenancy.context.TenantContextHolder;
import org.nirvikalpa.multitenancy.datasource.TenantRoutingDataSource;
import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;
import org.nirvikalpa.multitenancy.registry.InMemoryTenantRegistry;
import org.nirvikalpa.multitenancy.registry.JdbcTenantRegistry;
import org.nirvikalpa.multitenancy.registry.MultiTenantRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;


/**
 * Auto-configuration for multitenancy components.
 */
@AutoConfiguration
public class MultitenancyAutoConfiguration {

    @Bean
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
    @Primary
    public DataSource tenantRoutingDataSource(MultiTenantRegistry tenantRegistry, MultiTenancyProperties props) {
        TenantRoutingDataSource routingDataSource = new TenantRoutingDataSource(tenantRegistry, props);
        routingDataSource.setTargetDataSources(new HashMap<>()); // required to init
        routingDataSource.afterPropertiesSet();
        return routingDataSource;
    }
}
