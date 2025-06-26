package org.nirvikalpa.multitenancy.config;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;


@AutoConfiguration(after = MultitenancyAutoConfiguration.class)
@ConditionalOnClass(EntityManagerFactory.class)
@ConditionalOnProperty(prefix = "multi-tenancy", name = "enabled", matchIfMissing = true)
public class MultiTenantJpaAutoConfiguration {

    private final MultiTenancyProperties.Resolution resolution;

    public MultiTenantJpaAutoConfiguration(MultiTenancyProperties.Resolution resolution) {
        this.resolution = resolution;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource,
            JpaProperties jpaProperties,
            ObjectProvider<EntityManagerFactoryBuilder> builderProvider
    ) {
        Map<String, String> props = jpaProperties.getProperties();

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan(resolution.getPackagesToScan());
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emf.setJpaPropertyMap(props);

        return emf;
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}