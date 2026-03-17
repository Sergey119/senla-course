package com.example.task.util;

import com.example.task.config.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(HibernateConfig.class);
            sessionFactory = context.getBean("sessionFactory", SessionFactory.class);

            System.out.println("Hibernate SessionFactory created successfully from hibernate.cfg.xml");

        } catch (Exception e) {
            System.err.println("Hibernate initialize error: " + e);
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