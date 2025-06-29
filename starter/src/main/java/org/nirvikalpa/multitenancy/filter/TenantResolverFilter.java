package org.nirvikalpa.multitenancy.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.nirvikalpa.multitenancy.context.TenantContextHolder;
import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;
import org.nirvikalpa.multitenancy.resolver.TenantIdentifierResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TenantResolverFilter extends OncePerRequestFilter {

    private final TenantIdentifierResolver tenantIdentifierResolver;

    public TenantResolverFilter(TenantIdentifierResolver tenantIdentifierResolver) {
        this.tenantIdentifierResolver = tenantIdentifierResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tenantId = tenantIdentifierResolver.resolveTenantId(request);
        TenantContextHolder.setTenantId(tenantId);
        filterChain.doFilter(request, response);
        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContextHolder.clear(); // prevent thread leak
        }
    }

}
