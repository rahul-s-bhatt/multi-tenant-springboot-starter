package org.nirvikalpa.multitenancy.properties;

import org.nirvikalpa.multitenancy.enums.MultiTenantIdentifierResolverEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "multi-tenancy")
public class MultiTenancyProperties {
    private Boolean actuatorEnabled;
    private String defaultTenantId;
    private Isolation isolation = new Isolation();
    private Registry registry = new Registry();
    private Resolution resolution = new Resolution();

    // Getter
    public Boolean getActuatorEnabled() {return actuatorEnabled;}
    public void setActuatorEnabled(Boolean actuatorEnabled) { this.actuatorEnabled = actuatorEnabled;}
    public String getDefaultTenantId() {return defaultTenantId;}
    public void setDefaultTenantId(String defaultTenantId){this.defaultTenantId = defaultTenantId;}
    public Isolation getIsolation() {return isolation;}
    public Registry getRegistry() {return registry;}
    public Resolution getResolution(){return resolution;}

    public static class Isolation {
        private String type;
        private DatasourceTemplate datasourceTemplate = new DatasourceTemplate();

        public String getType(){return type;}
        public void setType(String type) {this.type =type;}
        public DatasourceTemplate getDatasourceTemplate() { return datasourceTemplate; }
        public void setDatasourceTemplate(DatasourceTemplate template) { this.datasourceTemplate = template; }

        public static class DatasourceTemplate {
            private String driverClassName;
            private Integer maxPoolSize;
            private Integer minIdle;
            private Long idleTimeout;
            private Long maxLifetime;

            public String getDriverClassName() { return driverClassName; }
            public void setDriverClassName(String driverClassName) { this.driverClassName = driverClassName; }

            public Integer getMaxPoolSize() { return maxPoolSize; }
            public void setMaxPoolSize(Integer maxPoolSize) { this.maxPoolSize = maxPoolSize; }

            public Integer getMinIdle() { return minIdle; }
            public void setMinIdle(Integer minIdle) { this.minIdle = minIdle; }

            public Long getIdleTimeout() { return idleTimeout; }
            public void setIdleTimeout(Long idleTimeout) { this.idleTimeout = idleTimeout; }

            public Long getMaxLifetime() { return maxLifetime; }
            public void setMaxLifetime(Long maxLifetime) { this.maxLifetime = maxLifetime; }
        }
    }

    public static class Registry {
        private String type;
        private Jdbc jdbc;
        private Map<String, InMemoryTenant> inMemoryTenants;

        public String getType(){return type;}
        public void setType(String type) {this.type =type;}
        public Jdbc getJdbc() { return jdbc; }
        public void setJdbc(Jdbc jdbc) { this.jdbc = jdbc; }
        public Map<String, InMemoryTenant> getInMemoryTenants() { return inMemoryTenants; }
        public void setInMemoryTenants(Map<String, InMemoryTenant> inMemoryTenants) { this.inMemoryTenants = inMemoryTenants; }

        public static class Jdbc {
            private String url;
            private String username;
            private String password;
            private String driverClassName;

            public String getUrl() { return url; }
            public void setUrl(String url) { this.url = url; }

            public String getUsername() { return username; }
            public void setUsername(String username) { this.username = username; }

            public String getPassword() { return password; }
            public void setPassword(String password) { this.password = password; }

            public String getDriverClassName() { return driverClassName; }
            public void setDriverClassName(String driverClassName) { this.driverClassName = driverClassName; }
        }

        public static class InMemoryTenant {
            private String tenantId;
            private String datasourceUrl;
            private String username;
            private String password;

            public InMemoryTenant() {}

            public InMemoryTenant(String tenantId, String datasourceUrl, String username, String password){
                this.tenantId = tenantId;
                this.datasourceUrl = datasourceUrl;
                this.username = username;
                this.password = password;
            }

            public String getTenantId() { return tenantId; }
            public void setTenantId(String tenantId) { this.tenantId = tenantId; }

            public String getDatasourceUrl() { return datasourceUrl; }
            public void setDatasourceUrl(String datasourceUrl) { this.datasourceUrl = datasourceUrl; }

            public String getUsername() { return username; }
            public void setUsername(String username) { this.username = username; }

            public String getPassword() { return password; }
            public void setPassword(String password) { this.password = password; }
        }
    }

    public static class Resolution {
        private Boolean enabled;
        private String type;
        private SubDomain subDomain = new SubDomain();

        public Boolean getEnabled() {return enabled;}
        public void setEnabled(Boolean enabled) {this.enabled = enabled;}
        public String getType(){return type;}
        public void setType(String type) {this.type =type;}
        public SubDomain getSubDomain() {return subDomain; }
        public void setSubDomain(SubDomain subDomain) { this.subDomain = subDomain; }

        public static class SubDomain {
            private String baseDomain;
            public String getBaseDomain() { return baseDomain; }
            public void setBaseDomain(String baseDomain) { this.baseDomain = baseDomain; }
        }
    }
}
