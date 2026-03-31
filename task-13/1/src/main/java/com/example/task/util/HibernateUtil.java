package com.example.task.util;

import com.example.task.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    static {
        try {
            // Загружаем конфигурацию из hibernate.cfg.xml
            Configuration configuration = new Configuration().configure();

            // Регистрируем аннотированные классы
            configuration.addAnnotatedClass(CarPlace.class);
            configuration.addAnnotatedClass(Customer.class);
            configuration.addAnnotatedClass(ServiceAdvisor.class);
            configuration.addAnnotatedClass(Technician.class);
            configuration.addAnnotatedClass(Order.class);

            // Создаем SessionFactory
            sessionFactory = configuration.buildSessionFactory();

            System.out.println("Hibernate SessionFactory created successfully from hibernate.cfg.xml");

        } catch (Exception e) {
            System.err.println("Ошибка инициализации Hibernate: " + e);
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session openSession() {
        return sessionFactory.openSession();
    }

    public static Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("Hibernate SessionFactory closed");
        }
    }
}