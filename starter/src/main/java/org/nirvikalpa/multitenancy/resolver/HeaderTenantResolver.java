package org.nirvikalpa.multitenancy.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.nirvikalpa.multitenancy.exception.NoTenantFoundException;

public class HeaderTenantResolver implements TenantResolver {
    @Override
    public String resolveTenantId(HttpServletRequest request) {
        String tenantId = request.getHeader("X-TENANT-ID");
        if (tenantId == null || tenantId.isBlank()) {
            throw new NoTenantFoundException("Tenant ID header 'X-TENANT-ID' not present in request.");
        }
        return tenantId;
    }
}
