package org.nirvikalpa.multitenancy.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.nirvikalpa.multitenancy.context.TenantContextHolder;
import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SubdomainTenantResolverFilter extends OncePerRequestFilter {

    private final MultiTenancyProperties.Resolution.SubDomain subDomain;

    public SubdomainTenantResolverFilter(MultiTenancyProperties.Resolution.SubDomain subDomain) {
        this.subDomain = subDomain;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String host = request.getServerName(); // e.g., tenant1.example.com
        String tenantId = extractSubdomain(host);
        TenantContextHolder.setTenantId(tenantId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContextHolder.clear();
        }
    }

    private String extractSubdomain(String host) {
        // Parse your base domain accordingly
        if (host == null || subDomain.getBaseDomain() == null || subDomain.getBaseDomain().isEmpty() || !host.contains(".")) return null;
        return host.split("\\.")[0]; // crude but works for v1
    }
}
