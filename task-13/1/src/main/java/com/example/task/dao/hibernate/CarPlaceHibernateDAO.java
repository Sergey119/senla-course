package com.example.task.dao.hibernate;

import com.example.task.exception.DaoException;
import com.example.task.model.CarPlace;
import com.example.task.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class CarPlaceHibernateDAO extends AbstractHibernateDAO<CarPlace, Integer> {

    public CarPlaceHibernateDAO() {
        super();
    }

    public List<CarPlace> findAvailableCarPlaces() {
        String hql = "FROM CarPlace cp WHERE cp.isOccupied = false";
        return executeQuery(hql);
    }

    public int countAvailableCarPlaces() {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            String hql = "SELECT COUNT(cp) FROM CarPlace cp WHERE cp.isOccupied = false";
            Query<Integer> query = session.createQuery(hql, Integer.class);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error in counting available car places", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void updateOccupation(Integer id, boolean isOccupied) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.openSession();
            transaction = session.beginTransaction();

            String hql = "UPDATE CarPlace cp SET cp.isOccupied = :occupied WHERE cp.id = :id";
            Query<?> query = session.createQuery(hql);
            query.setParameter("occupied", isOccupied);
            query.setParameter("id", id);
            query.executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error(e.getMessage());
            throw new DaoException("Error updating car place status", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<CarPlace> findByCarLift(boolean hasCarLift) {
        String hql = "FROM CarPlace cp WHERE cp.carLift = :carLift";
        return executeQuery(hql, hasCarLift);
    }
}