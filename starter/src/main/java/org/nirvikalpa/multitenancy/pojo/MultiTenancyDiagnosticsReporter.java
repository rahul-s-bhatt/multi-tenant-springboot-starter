package org.nirvikalpa.multitenancy.pojo;

import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class MultiTenancyDiagnosticsReporter {

    private final Environment env;
    private final MultiTenancyProperties properties;

    private final String IsolationTypeKey = "Isolation Type";
    private final String RegistryTypeKey = "Registry Type";
    private final String DefaultTenantIdKey = "Default Tenant Id";
    private final String ResolverTypeKey = "Resolver Type";
    private final String IsResolverEnabledKey = "Is Resolver Enabled";
    private final String IsActuatorEnabledKey = "Is Actuator Enabled";
    private final String IsActuatorExposedKey = "Is Actuator Exposed";
    private final String ActuatorEndpointKey = "Actuator Endpoint";

    public MultiTenancyDiagnosticsReporter(Environment env, MultiTenancyProperties properties) {
        this.env = env;
        this.properties = properties;
    }

    public MultiTenancyDiagnostics collect() {
        Boolean actuatorEnabled = properties.getActuatorEnabled();
        String isolationType = properties.getIsolation().getType();
        String registryType = properties.getRegistry().getType();
        String defaultTenantId = properties.getDefaultTenantId();
        Boolean getResolverEnabled = properties.getResolution().getEnabled();
        String resolverType = properties.getResolution().getType();

        String[] exposedEndpoints = env.getProperty("management.endpoints.web.exposure.include", String[].class, new String[0]);
        boolean actuatorExposed = Arrays.asList(exposedEndpoints).contains("multitenancy-status") || Arrays.asList(exposedEndpoints).contains("*");

        return new MultiTenancyDiagnostics(isolationType, registryType, getResolverEnabled, resolverType, defaultTenantId, actuatorEnabled, actuatorExposed);
    }

    public void printToConsole() {
        MultiTenancyDiagnostics diagnostics = collect();

        PrintStream out = System.out;

        out.println("────────────────────────────────────────────────────────────────────");
        out.println("📦 Multi-Tenancy Diagnostics:");
        out.println("✔️  Isolation Type:        " + diagnostics.isolationType());
        out.println("✔️  Registry Type:    " + diagnostics.registryType());
        out.println("✔️  Default Tenant Id:     " + diagnostics.defaultTenantId());

        if(!diagnostics.resolverEnabled()){
            out.println("Resolver is not enabled, if you want to enable it, do it by multi-tenancy.resolution.enabled = true");
        } else {
            String resolverType = properties.getResolution().getType();
            out.println("✔️  Resolver Type:    " + resolverType);
        }

        if(!diagnostics.actuatorEnabled())
        {
            out.println("\n💡 Actuator is not enabled, Tip: Enabled it by adding:\n");
            out.println("multi-tenancy:");
            out.println(" actuator-enabled: true");
        } else {
            if (!diagnostics.actuatorExposed()) {
                out.println("\n💡 Actuator is not exposed, Tip: Expose it by adding:\n");
                out.println("  management:");
                out.println("    endpoints:");
                out.println("      web:");
                out.println("        exposure:");
                out.println("          include: multitenancy-status");
            } else {
                out.println("🔗 Actuator:     /actuator/multitenancy-status");
            }
        }
        out.println("────────────────────────────────────────────────────────────────────");
    }

    public Map<String, Object> diagnostics() {
        MultiTenancyDiagnostics diagnostics = collect();

        Map<String, Object> map = new LinkedHashMap<>(); // maintain order, and mutable
        map.put("Enabled", true);
        map.put(IsolationTypeKey, diagnostics.isolationType());
        map.put(RegistryTypeKey, diagnostics.registryType());
        map.put(DefaultTenantIdKey, diagnostics.defaultTenantId());
        map.put(IsResolverEnabledKey, diagnostics.resolverEnabled());
        map.put(IsActuatorExposedKey, diagnostics.actuatorExposed());

        if (diagnostics.resolverEnabled()) {
            map.put(ResolverTypeKey, diagnostics.resolverType());
        }
        if (diagnostics.actuatorExposed()) {
            map.put(ActuatorEndpointKey, "/actuator/multitenancy-status");
        }

        return map;
    }

}
