package org.nirvikalpa.multitenancy.registry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JdbcTenantRegistryTest.Config.class)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:test",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password="
})
class JdbcTenantRegistryTest {

    @Configuration
    static class Config {
        @Bean
        DataSource dataSource() {
            return DataSourceBuilder.create()
                    .url("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
                    .username("sa")
                    .build();
        }
    }

    @Autowired
    DataSource dataSource;

    JdbcTenantRegistry registry;

    @BeforeEach
    void setup() throws Exception {
        try (var conn = dataSource.getConnection(); var stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE tenant (tenant_id VARCHAR PRIMARY KEY, datasource_url VARCHAR, username VARCHAR, password VARCHAR, schema_name VARCHAR)");
            stmt.execute("INSERT INTO tenant VALUES ('acme', 'jdbc:h2:mem:acme', 'sa', '', null)");
        }
        registry = new JdbcTenantRegistry(dataSource);
    }

    @Test
    void shouldFetchTenantFromJdbcSource() {
        var tenant = registry.findByTenantId("acme").orElseThrow();
        assertEquals("jdbc:h2:mem:acme", tenant.datasourceUrl());
    }
}
