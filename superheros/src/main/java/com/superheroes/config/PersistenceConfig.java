package com.superheroes.config;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource({"classpath:persistence.properties"})
public class PersistenceConfig {

    @Autowired
    private Environment env;

    public static final String CONFIG_ENTITIES_TO_MAP = "entitiesToMap";
    public static final String CONFIG_JDBC_DRIVER_CLASS_NAME = "spring.datasource.driverClassName";
    public static final String CONFIG_JDBC_URL = "spring.datasource.url";
    public static final String CONFIG_JDBC_USER = "spring.datasource.username";
    public static final String CONFIG_JDBC_PASS = "spring.datasource.password";

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[]{env.getProperty(CONFIG_ENTITIES_TO_MAP)});
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty(CONFIG_JDBC_DRIVER_CLASS_NAME));
        dataSource.setUrl(env.getProperty(CONFIG_JDBC_URL));
        dataSource.setUsername(env.getProperty(CONFIG_JDBC_USER));
        dataSource.setPassword(env.getProperty(CONFIG_JDBC_PASS));
        return dataSource;
    }

    @Bean
    @Autowired
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        hibernateProperties.setProperty("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
        return hibernateProperties;
    }
}