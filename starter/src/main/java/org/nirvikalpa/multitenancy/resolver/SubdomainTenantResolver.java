package org.nirvikalpa.multitenancy.resolver;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Default implementation that extracts tenant ID from subdomain.
 * For example, acme.example.com -> tenantId = "acme"
 */
public class SubdomainTenantResolver implements TenantResolver {

    private final String baseDomain;

    public SubdomainTenantResolver(String baseDomain) {
        this.baseDomain = baseDomain;
    }

    @Override
    public String resolveTenantId(HttpServletRequest request) {
        String host = request.getServerName();
        if (host == null || !host.endsWith(baseDomain)) {
            return null;
        }

        String[] segments = host.split("\\.");
        int baseParts = baseDomain.split("\\.").length;

        if (segments.length <= baseParts) {
            return null;
        }

        return segments[0]; // first segment = subdomain
    }
}
