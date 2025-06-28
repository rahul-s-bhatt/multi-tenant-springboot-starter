package org.nirvikalpa.multitenancy.actuators;

import org.nirvikalpa.multitenancy.pojo.MultiTenancyDiagnosticsReporter;
import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;
import org.nirvikalpa.multitenancy.registry.MultiTenantRegistry;
import org.nirvikalpa.multitenancy.registry.TenantDescriptor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

@Endpoint(id = "multitenancy-status")
@Component
public class MultiTenancyStatusEndpoint {

    private final MultiTenancyDiagnosticsReporter multiTenancyDiagnosticsReporter;

    public MultiTenancyStatusEndpoint(MultiTenancyDiagnosticsReporter multiTenancyDiagnosticsReporter) {
        this.multiTenancyDiagnosticsReporter = multiTenancyDiagnosticsReporter;
    }

    @ReadOperation
    public Map<String, Object> exposeMultiTenancyInfo() {
        multiTenancyDiagnosticsReporter.collect();
        return multiTenancyDiagnosticsReporter.diagnostics();
    }
}
