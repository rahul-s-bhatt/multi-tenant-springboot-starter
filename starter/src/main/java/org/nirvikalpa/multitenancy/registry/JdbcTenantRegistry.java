package org.nirvikalpa.multitenancy.registry;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class JdbcTenantRegistry implements MultiTenantRegistry {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<TenantDescriptor> rowMapper = (rs, rowNum) -> new TenantDescriptor(
            rs.getString("tenant_id"),
            rs.getString("datasource_url"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("schema_name")
    );

    private final Map<String, TenantDescriptor> cache = new ConcurrentHashMap<>();

    public JdbcTenantRegistry(DataSource registryDataSource) {
        this.jdbcTemplate = new JdbcTemplate(registryDataSource);
        this.refresh(); // preload cache
    }

    @Override
    public List<TenantDescriptor> getAllTenants() {
        return new ArrayList<>(cache.values());
    }

    @Override
    public Optional<TenantDescriptor> findByTenantId(String tenantId) {
        return Optional.ofNullable(cache.get(tenantId));
    }

    @Override
    public void refresh() {
        String sql = "SELECT tenant_id, datasource_url, username, password, schema_name FROM tenant";
        List<TenantDescriptor> tenants = jdbcTemplate.query(sql, rowMapper);
        cache.clear();
        tenants.forEach(t -> cache.put(t.tenantId(), t));
    }
}
