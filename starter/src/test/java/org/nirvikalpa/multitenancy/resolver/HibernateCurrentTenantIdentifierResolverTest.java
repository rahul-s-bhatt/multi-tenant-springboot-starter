package org.nirvikalpa.multitenancy.resolver;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.junit.jupiter.api.Test;
import org.nirvikalpa.multitenancy.context.TenantContextHolder;

import static org.junit.jupiter.api.Assertions.*;

class HibernateCurrentTenantIdentifierResolverTest {

    @Test
    void shouldResolveTenantIdFromContext() {
        TenantContextHolder.setTenantId("tenantABC");
        CurrentTenantIdentifierResolver<String> resolver = new HibernateCurrentTenantIdentifierResolver();
        assertEquals("tenantABC", resolver.resolveCurrentTenantIdentifier());
        TenantContextHolder.clear();
    }
}