//package org.nirvikalpa.multitenancy.config;
//
//import org.junit.jupiter.api.Test;
//import org.nirvikalpa.multitenancy.enums.MultiTenantIdentifierResolverEnum;
//import org.nirvikalpa.multitenancy.filter.TenantResolverFilter;
//import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;
//import org.nirvikalpa.multitenancy.resolver.TenantIdentifierResolver;
//import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.runner.ApplicationContextRunner;
//import org.springframework.test.context.TestPropertySource;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
////@TestPropertySource(locations = "classpath:test-application.yml")
////@SpringBootTest
//public class MultiTenancyAutoConfigurationTest {
//
//    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
//            .withUserConfiguration(DummyApp.class)
//            .withPropertyValues(
//                    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
//                    "multi-tenancy.enabled=true",
//                    "multi-tenancy.registry.type=IN_MEMORY",
//                    "multi-tenancy.registry.in-memory-tenants[0].tenantId=acme",
//                    "multi-tenancy.registry.in-memory-tenants[0].datasourceUrl=jdbc:h2:mem:acme",
//                    "multi-tenancy.registry.in-memory-tenants[0].username=sa",
//                    "multi-tenancy.registry.in-memory-tenants[0].password=",
//                    "multi-tenancy.registry.in-memory-tenants[1].tenantId=globex",
//                    "multi-tenancy.registry.in-memory-tenants[1].datasourceUrl=jdbc:h2:mem:globex",
//                    "multi-tenancy.registry.in-memory-tenants[1].username=sa",
//                    "multi-tenancy.registry.in-memory-tenants[1].password=",
//                    "multi-tenancy.default-tenant-id=acme",
//                    "multi-tenancy.resolution.enabled=true",
//                    "multi-tenancy.resolution.type=HTTP_HEADER"
//            );
//
//    @Test
//    void shouldRegisterTenantIdentifierResolverBean() {
//        contextRunner.run(context -> {
//            assertThat(context).hasSingleBean(TenantIdentifierResolver.class);
//        });
//    }
//
//    @Test
//    void shouldRegisterTenantResolverFilterBean() {
//        contextRunner.run(context -> {
//            assertThat(context).hasSingleBean(TenantResolverFilter.class);
//        });
//    }
//
//    @Test
//    void shouldLoadMultiTenancyProperties() {
//        contextRunner.run(context -> {
//            MultiTenancyProperties props = context.getBean(MultiTenancyProperties.class);
//            assertThat(props).isNotNull();
//            assertThat(props.getResolution().getType()).isEqualTo(MultiTenantIdentifierResolverEnum.HTTP_HEADER.toString());
//        });
//    }
//
//    @SpringBootApplication
//    @ImportAutoConfiguration(classes = {
//            MultiTenancyAutoConfiguration.class,
//            MultiTenantResolutionAutoConfiguration.class,
//            MultiTenancyResolutionValidatorConfig.class
//    })
//    static class DummyApp {
//        // No beans; context runner will activate auto-config
//    }
//}
