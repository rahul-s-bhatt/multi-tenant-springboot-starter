package org.nirvikalpa.multitenancy.config;

import org.nirvikalpa.multitenancy.context.DefaultTenantContext;
import org.nirvikalpa.multitenancy.context.TenantContext;
import org.nirvikalpa.multitenancy.context.TenantContextHolder;
import org.nirvikalpa.multitenancy.resolver.SubdomainTenantResolver;
import org.nirvikalpa.multitenancy.resolver.TenantResolver;
import org.nirvikalpa.multitenancy.web.TenantFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;


/**
 * Auto-configuration for multitenancy components.
 */
@AutoConfiguration
public class MultitenancyAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TenantContext tenantContext() {
        DefaultTenantContext context = new DefaultTenantContext();
        TenantContextHolder.setContext(context);
        return context;
    }

    @Bean
    @ConditionalOnMissingBean
    public TenantResolver tenantResolver() {
        // Default base domain can be overridden with properties later
        return new SubdomainTenantResolver("example.com");
    }

    @Bean
    @ConditionalOnMissingBean
    public FilterRegistrationBean<TenantFilter> tenantFilter(TenantResolver tenantResolver) {
        FilterRegistrationBean<TenantFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new TenantFilter(tenantResolver));
        registration.setOrder(1); // Ensure it runs early
        registration.addUrlPatterns("/*");
        return registration;
    }
}
