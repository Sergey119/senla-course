package com.example.task.dao.hibernate;

import com.example.task.exception.DaoException;
import com.example.task.model.Customer;
import com.example.task.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class CustomerHibernateDAO extends AbstractHibernateDAO<Customer, Integer> {

    public CustomerHibernateDAO() {
        super();
    }

    public int countAll() {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            String hql = "SELECT COUNT(c) FROM Customer c";
            Query<Integer> query = session.createQuery(hql, Integer.class);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Customer counting error", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<Customer> findAllSortedByName() {
        String hql = "FROM Customer c ORDER BY c.name";
        return executeQuery(hql);
    }


    public void updateName(Integer id, String newName) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.openSession();
            transaction = session.beginTransaction();

            String hql = "UPDATE Customer c SET c.name = :name WHERE c.id = :id";
            Query<?> query = session.createQuery(hql);
            query.setParameter("name", newName);
            query.setParameter("id", id);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error(e.getMessage());
            throw new DaoException("Error updating customer name", e);
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
            String hql = "SELECT COUNT(c) FROM Customer c WHERE LOWER(c.name) = LOWER(:name)";
            Query<Integer> query = session.createQuery(hql, Integer.class);
            query.setParameter("name", name);
            Integer count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error checking existence of customer by name", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

}