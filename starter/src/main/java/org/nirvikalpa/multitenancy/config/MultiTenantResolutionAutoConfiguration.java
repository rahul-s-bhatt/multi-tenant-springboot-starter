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
    @ConditionalOnMissingBean(TenantIdentifierResolver.class)
    @ConditionalOnProperty(name = "multi-tenancy.resolution.type", havingValue = "DOMAIN")
    public TenantIdentifierResolver domainResolver(){
        return new DomainTenantIdentifierResolver();
    }

    @Bean
    @ConditionalOnMissingBean(TenantIdentifierResolver.class)
    @ConditionalOnProperty(name = "multi-tenancy.resolution.type", havingValue = "HTTP_HEADER")
    public TenantIdentifierResolver httpHeaderResolver(){
        return new HttpHeaderTenantIdentifierResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public FilterRegistrationBean<TenantResolverFilter> tenantResolverFilterFilter(TenantIdentifierResolver tenantIdentifierResolver){
        TenantResolverFilter filter = new TenantResolverFilter(tenantIdentifierResolver);
        System.out.println("✅ Registering TenantResolverFilter");

        FilterRegistrationBean<TenantResolverFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(filter);
        bean.setOrder(1); // before Spring Security
        return bean;
    }
}
