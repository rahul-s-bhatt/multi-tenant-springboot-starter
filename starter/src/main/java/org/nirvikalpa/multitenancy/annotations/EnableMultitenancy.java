package org.nirvikalpa.multitenancy.annotations;

import org.nirvikalpa.multitenancy.config.MultitenancyAutoConfiguration;
import org.nirvikalpa.multitenancy.config.TenantRegistryAutoConfig;
import org.nirvikalpa.multitenancy.config.TenantDataSourceAutoConfig;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.lang.annotation.*;

/**
 * Enables multitenancy starter auto-configuration in a Spring Boot application.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ImportAutoConfiguration({
        MultitenancyAutoConfiguration.class,
        TenantRegistryAutoConfig.class,
        TenantDataSourceAutoConfig.class
})
public @interface EnableMultitenancy {
}

