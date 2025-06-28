package org.nirvikalpa.multitenancy.config;

import org.nirvikalpa.multitenancy.filter.TenantResolverFilter;
import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;
import org.nirvikalpa.multitenancy.resolver.DomainTenantIdentifierResolver;
import org.nirvikalpa.multitenancy.resolver.HttpHeaderTenantIdentifierResolver;
import org.nirvikalpa.multitenancy.resolver.TenantIdentifierResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "multi-tenancy.resolution.enabled", havingValue = "true")
@EnableConfigurationProperties(MultiTenancyProperties.class)
public class MultiTenantResolutionAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(TenantResolverFilter.class)
    @ConditionalOnProperty(name = "multi-tenancy.resolution.type", havingValue = "DOMAIN")
    public TenantIdentifierResolver domainResolver(){
        return new DomainTenantIdentifierResolver();
    }

    @ConditionalOnProperty(name = "multi-tenancy.resolution.type", havingValue = "HTTP_HEADER")
    public TenantIdentifierResolver httpHeaderResolver(){
        return new HttpHeaderTenantIdentifierResolver();
    }

    @Bean
    public FilterRegistrationBean<TenantResolverFilter> tenantResolverFilterFilter(TenantIdentifierResolver tenantIdentifierResolver){
        FilterRegistrationBean<TenantResolverFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new TenantResolverFilter(tenantIdentifierResolver));
        bean.setOrder(1); // ensure running it before security
        return bean;
    }
}
