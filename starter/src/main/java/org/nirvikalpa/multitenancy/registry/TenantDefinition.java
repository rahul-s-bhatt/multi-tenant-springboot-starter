package org.nirvikalpa.multitenancy.registry;

import jakarta.validation.constraints.NotBlank;

/**
 * Tenant metadata holder (e.g., DB URL, secrets).
 */
public record TenantDefinition(
        @NotBlank String tenantId,
        @NotBlank String datasourceUrl,
        @NotBlank String username,
        @NotBlank String password
) {}
