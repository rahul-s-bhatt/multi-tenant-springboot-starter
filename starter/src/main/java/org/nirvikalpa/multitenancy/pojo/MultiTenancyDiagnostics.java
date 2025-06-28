package org.nirvikalpa.multitenancy.pojo;

public record MultiTenancyDiagnostics(
        String isolationType,
        String registryType,
        boolean resolverEnabled,
        String resolverType,
        String defaultTenantId,
        boolean actuatorEnabled,
        boolean actuatorExposed
) {}
