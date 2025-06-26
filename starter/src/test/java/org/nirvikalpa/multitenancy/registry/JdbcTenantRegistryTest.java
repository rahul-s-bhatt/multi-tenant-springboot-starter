package org.nirvikalpa.multitenancy.registry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JdbcTenantRegistry.class)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:registry;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password="
})
class JdbcTenantRegistryTest {

    @Autowired
    DataSource dataSource;

    @BeforeEach
    void setup() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE tenant (tenant_id VARCHAR PRIMARY KEY, datasource_url VARCHAR, username VARCHAR, password VARCHAR, schema_name VARCHAR)");
            stmt.execute("INSERT INTO tenant VALUES ('acme', 'jdbc:h2:mem:acme', 'sa', '', null)");
        }
    }

    @Test
    void shouldFetchTenantFromJdbcSource() {
        JdbcTenantRegistry registry = new JdbcTenantRegistry(dataSource);
        TenantDescriptor tenant = registry.findByTenantId("acme").orElseThrow();
        assertEquals("jdbc:h2:mem:acme", tenant.datasourceUrl());
    }
}
