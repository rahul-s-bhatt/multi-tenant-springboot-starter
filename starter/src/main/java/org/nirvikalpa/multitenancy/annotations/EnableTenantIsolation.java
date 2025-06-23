package org.nirvikalpa.multitenancy.annotations;

import org.nirvikalpa.multitenancy.enums.TenantIsolationStrategy;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(TenantIsolationRegistrar.class)
public @interface EnableTenantIsolation {
    TenantIsolationStrategy strategy() default TenantIsolationStrategy.DATABASE;
}