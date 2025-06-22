package org.nirvikalpa.multitenancy.config;

import org.nirvikalpa.multitenancy.datasource.DataSourceFactory;
import org.nirvikalpa.multitenancy.datasource.TenantRoutingDataSource;
import org.nirvikalpa.multitenancy.properties.HikariTenantPoolProperties;
import org.nirvikalpa.multitenancy.registry.TenantRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(HikariTenantPoolProperties.class)
public class TenantDataSourceAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public DataSourceFactory dataSourceFactory(HikariTenantPoolProperties poolProperties) {
        return new DataSourceFactory(poolProperties);
    }

    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    public DataSource dataSource(TenantRegistry tenantRegistry, DataSourceFactory dataSourceFactory) {
        return new TenantRoutingDataSource(tenantRegistry, dataSourceFactory);
    }
}
