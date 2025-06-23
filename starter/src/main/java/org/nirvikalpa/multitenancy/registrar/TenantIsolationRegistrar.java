package org.nirvikalpa.multitenancy.registrar;


import org.nirvikalpa.multitenancy.annotations.EnableTenantIsolation;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

public class TenantIsolationRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, org.springframework.beans.factory.support.BeanDefinitionRegistry registry) {
        Map<String, Object> attrs = metadata.getAnnotationAttributes(EnableTenantIsolation.class.getName());
        if (attrs == null) return;

        Object strategy = attrs.get("strategy");
        if (strategy == null) return;

        // Set tenant.strategy as a Spring Environment property
        if (environment instanceof ConfigurableEnvironment configurableEnvironment) {
            MutablePropertySources sources = configurableEnvironment.getPropertySources();

            Map<String, Object> tenantProps = Map.of("tenant.strategy", strategy.toString());
            MapPropertySource source = new MapPropertySource("tenant-isolation-annotation", tenantProps);

            // Add early so it can be overridden by application.yml
            sources.addFirst(source);
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}