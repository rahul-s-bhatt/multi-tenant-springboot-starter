package org.nirvikalpa.multitenancy.properties;

import lombok.Data;
import org.nirvikalpa.multitenancy.enums.MultiTenantIsolationStrategyEnum;
import org.nirvikalpa.multitenancy.enums.MultiTenantRegistryTypeEnum;
import org.nirvikalpa.multitenancy.enums.MultiTenantResolutionStrategyEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "multi-tenancy")
@Data
public class MultiTenancyProperties {

    private Registry registry = new Registry();
    private Resolution resolution = new Resolution();
    private Isolation isolation = new Isolation();
    private Microservice microservice = new Microservice();

    @Data
    public static class Registry {
        private MultiTenantRegistryTypeEnum type;
        private Jdbc jdbc;
        private Map<String, InMemoryTenant> inMemoryTenants = new HashMap<>();

        @Data
        public static class Jdbc {
            private String url;
            private String username;
            private String password;
            private String driverClassName;
        }

        @Data
        public static class InMemoryTenant {
            private String tenantId;
            private String datasourceUrl;
            private String username;
            private String password;

            public InMemoryTenant(String tenantId, String datasourceUrl, String username, String password) {
                this.tenantId = tenantId;
                this.datasourceUrl = datasourceUrl;
                this.username = username;
                this.password = password;
            }
        }
    }

    @Data
    public static class Resolution {
        private MultiTenantResolutionStrategyEnum strategy;
        private SubDomain subDomain;

        @Data
        public static class SubDomain {
            private String baseDomain;
        }
    }

    @Data
    public static class Isolation {
        private MultiTenantIsolationStrategyEnum strategy;
        private DatasourceTemplate datasourceTemplate;

        @Data
        public static class DatasourceTemplate {
            private String driverClassName;
            private Integer maxPoolSize;
            private Integer minIdle;
            private Long idleTimeout;
            private Long maxLifetime;
        }
    }

    @Data
    public static class Microservice {
        private boolean enabled;
        private String dbStrategy; // consider an enum too
    }
}
