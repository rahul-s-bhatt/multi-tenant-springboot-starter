package org.nirvikalpa.multitenancy.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.nirvikalpa.multitenancy.exception.HostNotFoundException;
import org.nirvikalpa.multitenancy.exception.NoTenantFoundException;

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
            throw new HostNotFoundException("Request host not found or invalid: " + host);
        }

        String[] segments = host.split("\\.");
        String[] baseParts = baseDomain.split("\\.");

        if (segments.length <= baseParts.length) {
            throw new NoTenantFoundException("No subdomain found in host: " + host);
        }

        int tenantIndex = segments.length - baseParts.length - 1;
        return segments[tenantIndex]; // Gets the subdomain before the base
    }

}
