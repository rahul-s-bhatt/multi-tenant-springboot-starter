package org.nirvikalpa.multitenancy.registry;

public record TenantDescriptor(
        String tenantId,
        String datasourceUrl,
        String username,
        String password,
        String schemaName // nullable for per-db strategy
) {}