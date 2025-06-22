package org.nirvikalpa.multitenancy.config;

import org.nirvikalpa.multitenancy.properties.TenantProperties;
import org.nirvikalpa.multitenancy.registry.InMemoryTenantRegistry;
import org.nirvikalpa.multitenancy.registry.TenantRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TenantProperties.class)
public class TenantRegistryAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public TenantRegistry inMemoryTenantRegistry(TenantProperties tenantProperties) {
        return new InMemoryTenantRegistry(tenantProperties.getTenants());
    }
}
