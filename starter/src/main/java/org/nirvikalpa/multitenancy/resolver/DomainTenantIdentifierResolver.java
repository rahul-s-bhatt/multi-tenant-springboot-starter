package org.nirvikalpa.multitenancy.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.nirvikalpa.multitenancy.exceptions.MissingTenantException;

public class DomainTenantIdentifierResolver implements TenantIdentifierResolver {

    @Override
    public String resolveTenantId(HttpServletRequest request) {
        String host = request.getServerName();
        if(host == null || !host.contains(".")) {
            throw new MissingTenantException("Missing or in proper Host, so unable to resolve tenant.");
        }
        return host;
    }
}
