package dev.bscako.blaucher.configuration.sqlite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:sqlite.properties")
@EnableJpaRepositories(basePackages = SQLiteConfig.PACKAGE_REPOSITORIES)
public class SQLiteConfig {

    public static final String PACKAGE_REPOSITORIES = "dev.bscako.blaucher.models.repositories";
    private static final String PACKAGE_MODELS = "dev.bscako.blaucher.models";
    private static final String HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String HIBERNATE_HBM2DLL_AUTO = "hibernate.hbm2ddl.auto";
    private static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(this.env.getProperty("driverClassName"));
        dataSource.setUrl(this.env.getProperty("url"));
        dataSource.setUsername(this.env.getProperty("user"));
        dataSource.setPassword(this.env.getProperty("password"));
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(this.dataSource());
        em.setPackagesToScan(PACKAGE_MODELS);
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(this.additionalProperties());
        return em;
    }

    final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        if (this.env.getProperty(HIBERNATE_HBM2DLL_AUTO) != null) {
            hibernateProperties.setProperty(HIBERNATE_HBM2DLL_AUTO, this.env.getProperty(HIBERNATE_HBM2DLL_AUTO));
        }
        if (this.env.getProperty(HIBERNATE_DIALECT) != null) {
            hibernateProperties.setProperty(HIBERNATE_DIALECT, this.env.getProperty(HIBERNATE_DIALECT));
        }
        if (this.env.getProperty(HIBERNATE_SHOW_SQL) != null) {
            hibernateProperties.setProperty(HIBERNATE_SHOW_SQL, this.env.getProperty(HIBERNATE_SHOW_SQL));
        }
        return hibernateProperties;
    }

}
