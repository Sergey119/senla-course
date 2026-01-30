package com.example.task.dao.hibernate;

import com.example.task.exception.DaoException;
import com.example.task.model.ServiceAdvisor;
import com.example.task.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ServiceAdvisorHibernateDAO extends AbstractHibernateDAO<ServiceAdvisor, Integer> {

    public ServiceAdvisorHibernateDAO() {
        super();
    }

    public long countAll() {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            String hql = "SELECT COUNT(sa) FROM ServiceAdvisor sa";
            Query<Long> query = session.createQuery(hql, Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Service advisor counting error", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<ServiceAdvisor> findAllSortedByName() {
        String hql = "FROM ServiceAdvisor sa ORDER BY sa.name";
        return executeQuery(hql);
    }

    public void updateName(Integer id, String newName) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.openSession();
            transaction = session.beginTransaction();

            String hql = "UPDATE ServiceAdvisor sa SET sa.name = :name WHERE sa.id = :id";
            Query<?> query = session.createQuery(hql);
            query.setParameter("name", newName);
            query.setParameter("id", id);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error(e.getMessage());
            throw new DaoException("Error updating service advisor name", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public boolean existsByName(String name) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            String hql = "SELECT COUNT(sa) FROM ServiceAdvisor sa WHERE LOWER(sa.name) = LOWER(:name)";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("name", name);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error checking existence of service advisor by name", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

}