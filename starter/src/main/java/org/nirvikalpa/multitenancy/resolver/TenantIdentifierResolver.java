package org.nirvikalpa.multitenancy.resolver;

import jakarta.servlet.http.HttpServletRequest;

public interface TenantIdentifierResolver {
    String resolveTenantId(HttpServletRequest request);
}
