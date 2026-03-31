package com.example.task.dao.hibernate;

import com.example.task.exception.DaoException;
import com.example.task.model.Technician;
import com.example.task.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class TechnicianHibernateDAO extends AbstractHibernateDAO<Technician, Integer> {

    public TechnicianHibernateDAO() {
        super();
    }

    public List<Technician> findAvailableTechnicians() {
        String hql = "FROM Technician t WHERE t.isAvailable = true";
        return executeQuery(hql);
    }

    public List<Technician> findAllSortedByName() {
        String hql = "FROM Technician t ORDER BY t.name";
        return executeQuery(hql);
    }

    public List<Technician> findAllSortedByAvailability() {
        String hql = "FROM Technician t ORDER BY t.isAvailable DESC, t.name";
        return executeQuery(hql);
    }

    public void updateAvailability(Integer technicianId, boolean isAvailable) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.openSession();
            transaction = session.beginTransaction();

            String hql = "UPDATE Technician t SET t.isAvailable = :available WHERE t.id = :id";
            Query<?> query = session.createQuery(hql);
            query.setParameter("available", isAvailable);
            query.setParameter("id", technicianId);
            int updated = query.executeUpdate();

            transaction.commit();

            System.out.println("Обновлено записей: " + updated);

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error(e.getMessage());
            throw new DaoException("Ошибка обновления статуса доступности техника", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void updateSpecialization(Integer technicianId, String newSpecialization) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.openSession();
            transaction = session.beginTransaction();

            String hql = "UPDATE Technician t SET t.specialization = :spec WHERE t.id = :id";
            Query<?> query = session.createQuery(hql);
            query.setParameter("spec", newSpecialization);
            query.setParameter("id", technicianId);
            int updated = query.executeUpdate();

            transaction.commit();

            System.out.println("Обновлено записей: " + updated);

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error(e.getMessage());
            throw new DaoException("Ошибка обновления специализации техника", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

}