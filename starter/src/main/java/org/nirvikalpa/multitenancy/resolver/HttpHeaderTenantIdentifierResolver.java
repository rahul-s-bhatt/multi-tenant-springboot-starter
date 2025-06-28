package org.nirvikalpa.multitenancy.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.nirvikalpa.multitenancy.exceptions.MissingTenantException;

public class HttpHeaderTenantIdentifierResolver implements  TenantIdentifierResolver {
    private static final String TENANT_HTTP_HEADER_KEY = "X-Tenant-ID";

    @Override
    public String resolveTenantId(HttpServletRequest request) {
        var tenantId = request.getHeader(TENANT_HTTP_HEADER_KEY);
        if(tenantId == null || tenantId.isBlank()){
            throw new MissingTenantException("Missing or empty tenant ID in header: " + TENANT_HTTP_HEADER_KEY);
        }
        return tenantId;
    }
}
