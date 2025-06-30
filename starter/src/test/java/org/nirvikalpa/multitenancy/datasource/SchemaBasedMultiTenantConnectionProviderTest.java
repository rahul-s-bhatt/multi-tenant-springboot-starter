//package org.nirvikalpa.multitenancy.datasource;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;
//import org.nirvikalpa.multitenancy.registry.MultiTenantRegistry;
//import org.nirvikalpa.multitenancy.registry.TenantDescriptor;
//
//import java.sql.SQLException;
//import java.util.Optional;
//
//import static org.junit.Assert.assertNotNull;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
// NOTE: Schema switching not implemented for driver: org.h2.Driver
//class SchemaBasedMultiTenantConnectionProviderTest {
//
//    private MultiTenantRegistry registry;
//    private MultiTenancyProperties props;
//
//    @BeforeEach
//    void setup() {
//        registry = mock(MultiTenantRegistry.class);
//        props = new MultiTenancyProperties();
//        props.setDefaultTenantId("public");
//        props.getIsolation().getDatasourceTemplate().setDriverClassName("org.h2.Driver");
//    }
//
//    @Test
//    void shouldSwitchSchemaPerTenant() throws SQLException {
//        var descriptor = new TenantDescriptor("public", "jdbc:h2:mem:testdb", "sa", "", null);
//        when(registry.findByTenantId("public")).thenReturn(Optional.of(descriptor));
//
//        var provider = new SchemaBasedMultiTenantConnectionProvider(registry, props);
//        var conn = provider.getConnection("myschema");
//
//        assertNotNull(conn);
//        conn.close();
//    }
//}