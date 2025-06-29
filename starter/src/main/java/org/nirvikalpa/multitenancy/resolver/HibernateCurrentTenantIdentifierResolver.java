package org.nirvikalpa.multitenancy.resolver;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.nirvikalpa.multitenancy.context.TenantContextHolder;
import org.nirvikalpa.multitenancy.exceptions.MissingTenantException;

public class HibernateCurrentTenantIdentifierResolver implements CurrentTenantIdentifierResolver<String> {

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId.isBlank()) {
            throw new MissingTenantException("Tenant ID missing");
        }
        return tenantId;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
