package org.nirvikalpa.multitenancy.execution;


import org.nirvikalpa.multitenancy.context.TenantContextHolder;
import org.nirvikalpa.multitenancy.registry.TenantDefinition;
import org.nirvikalpa.multitenancy.registry.TenantRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Service that executes a block of logic for every known tenant.
 */
@Service
public class TenantExecutionService {

    private static final Logger log = LoggerFactory.getLogger(TenantExecutionService.class);
    private final TenantRegistry tenantRegistry;

    public TenantExecutionService(TenantRegistry tenantRegistry) {
        this.tenantRegistry = tenantRegistry;
    }

    /**
     * Executes the given consumer for every tenant.
     * Automatically sets and clears the tenant context.
     */
    public void executeForAllTenants(Consumer<String> tenantAction) {
        for (Map.Entry<String, TenantDefinition> entry : tenantRegistry.findAllTenants().entrySet()) {
            String tenantId = entry.getKey();
            try {
                TenantContextHolder.setTenant(tenantId);
                tenantAction.accept(tenantId);
            } catch (Exception e) {
                log.error("Error executing for tenant: {}", tenantId, e);
            } finally {
                TenantContextHolder.clear();
            }
        }
    }
}
