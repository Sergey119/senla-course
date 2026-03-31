package com.example.task.config;

import com.example.task.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@org.springframework.context.annotation.Configuration
@PropertySource({"classpath:hibernate.properties"})
public class HibernateConfig {

    @Value("${hibernate.connection.driver_class}")
    private String driverClass;

    @Value("${hibernate.connection.url}")
    private String url;

    @Value("${hibernate.connection.username}")
    private String username;

    @Value("${hibernate.connection.password}")
    private String password;

    @Value("${hibernate.show_sql}")
    private String showSql;

    @Value("${hibernate.format_sql}")
    private String formatSql;

    @Value("${hibernate.current_session_context_class}")
    private String currentSessionContextClass;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public SessionFactory sessionFactory() {
        try {
            Configuration configuration = getConfiguration();

            SessionFactory sessionFactory = configuration.buildSessionFactory();
            System.out.println("Hibernate SessionFactory created successfully");

            return sessionFactory;

        } catch (Exception e) {
            System.err.println("Hibernate initialize error: " + e);
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    private Configuration getConfiguration() {
        Configuration configuration = new Configuration();

        configuration.setProperty("hibernate.connection.driver_class", driverClass);
        configuration.setProperty("hibernate.connection.url", url);
        configuration.setProperty("hibernate.connection.username", username);
        configuration.setProperty("hibernate.connection.password", password);
        configuration.setProperty("hibernate.show_sql", showSql);
        configuration.setProperty("hibernate.format_sql", formatSql);
        configuration.setProperty("hibernate.current_session_context_class", currentSessionContextClass);

        // configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        configuration.addAnnotatedClass(CarPlace.class);
        configuration.addAnnotatedClass(Customer.class);
        configuration.addAnnotatedClass(ServiceAdvisor.class);
        configuration.addAnnotatedClass(Technician.class);
        configuration.addAnnotatedClass(Order.class);

        return configuration;
    }
}