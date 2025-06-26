package org.nirvikalpa.multitenancy.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.function.Supplier;

public class DelayedTenantRoutingDataSource extends AbstractRoutingDataSource {

    private final Supplier<String> tenantIdSupplier;

    public DelayedTenantRoutingDataSource(Supplier<String> tenantIdSupplier) {
        this.tenantIdSupplier = tenantIdSupplier;
        setTargetDataSources(new HashMap<>()); // prevent NPE
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String tenantId = tenantIdSupplier.get();
        if (tenantId == null) {
            throw new IllegalStateException("No tenant ID set in context");
        }
        return tenantId;
    }
}