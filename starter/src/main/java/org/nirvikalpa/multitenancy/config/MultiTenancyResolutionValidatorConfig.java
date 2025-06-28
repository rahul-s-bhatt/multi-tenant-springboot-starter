package org.nirvikalpa.multitenancy.config;

import org.nirvikalpa.multitenancy.filter.TenantResolverFilter;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "multi-tenancy.resolution.enabled", havingValue = "false", matchIfMissing = false)
public class MultiTenancyResolutionValidatorConfig {

    @Bean
    public SmartInitializingSingleton tenantFilterPresenceValidator(ApplicationContext context) {
        return () -> {
            // Check if user provided a TenantResolverFilter bean manually
            String[] beanNames = context.getBeanNamesForType(TenantResolverFilter.class);
            if (beanNames.length == 0) {
                throw new IllegalStateException("""
                    Multi-tenancy resolution is disabled (multi-tenancy.resolution.enabled=false), 
                    but no custom TenantResolverFilter was found in the context.

                    ➤ You must define your own TenantResolverFilter bean explicitly 
                      if you opt out of the built-in resolution mechanism.
                """);
            }
        };
    }
}

