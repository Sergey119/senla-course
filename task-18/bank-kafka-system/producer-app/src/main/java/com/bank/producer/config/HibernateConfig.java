package com.bank.producer.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {
    private static final Logger logger = LogManager.getLogger(HibernateConfig.class);

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.username}")
    private String dbUsername;

    @Value("${db.password}")
    private String dbPassword;

    @Value("${db.driver.class}")
    private String dbDriverClass;

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Value("${hibernate.show_sql}")
    private String hibernateShowSql;

    @Value("${hibernate.hbm2ddl.auto}")
    private String hibernateHbm2DdlAuto;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dbDriverClass);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        logger.info("DataSource configured with URL: {}", dbUrl);
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.bank.common.model");

        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", hibernateDialect);
        hibernateProperties.put("hibernate.show_sql", hibernateShowSql);
        hibernateProperties.put("hibernate.hbm2ddl.auto", hibernateHbm2DdlAuto);
        hibernateProperties.put("hibernate.connection.autocommit", false);

        sessionFactory.setHibernateProperties(hibernateProperties);
        logger.info("SessionFactory configured with dialect: {}", hibernateDialect);

        return sessionFactory;
    }

    @Bean
    public SessionFactory getSessionFactory() {
        return sessionFactory().getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(getSessionFactory());
        logger.info("TransactionManager configured");
        return txManager;
    }
}