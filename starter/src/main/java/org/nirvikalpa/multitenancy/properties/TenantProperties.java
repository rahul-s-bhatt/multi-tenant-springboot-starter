package org.nirvikalpa.multitenancy.properties;

import org.nirvikalpa.multitenancy.enums.TenantIsolationStrategy;
import org.nirvikalpa.multitenancy.registry.TenantDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "tenancy")
@Validated
public class TenantProperties {
    private TenantIsolationStrategy strategy = TenantIsolationStrategy.DATABASE;
    /**
     * Static in-memory map of tenants loaded from YAML.
     */
    private Map<@NotBlank String, TenantDefinition> tenants = new HashMap<>();

    public Map<String, TenantDefinition> getTenants() {
        return tenants;
    }

    public void setTenants(Map<String, TenantDefinition> tenants) {
        this.tenants = tenants;
    }
}
