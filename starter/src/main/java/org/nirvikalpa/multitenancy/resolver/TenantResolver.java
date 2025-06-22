package org.nirvikalpa.multitenancy.resolver;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Strategy interface to resolve tenant ID from an incoming HTTP request.
 */
public interface TenantResolver {

    /**
     * Resolve the tenant ID from the given request.
     * @param request incoming HTTP request
     * @return tenant identifier or null if not resolvable
     */
    String resolveTenantId(HttpServletRequest request);
}
