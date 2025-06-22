package org.nirvikalpa.multitenancy.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tenancy.datasource")
public class HikariTenantPoolProperties {

    /**
     * Maximum number of connections in each tenant pool.
     */
    private int maxPoolSize = 5;

    /**
     * Minimum idle connections per tenant pool.
     */
    private int minIdle = 1;

    /**
     * Idle timeout in milliseconds.
     */
    private long idleTimeout = 600000;

    /**
     * Max lifetime of a connection.
     */
    private long maxLifetime = 1800000;

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public long getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(long idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public long getMaxLifetime() {
        return maxLifetime;
    }

    public void setMaxLifetime(long maxLifetime) {
        this.maxLifetime = maxLifetime;
    }
}
