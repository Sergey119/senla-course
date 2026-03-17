package com.example.task.dao;

import com.example.task.exception.DaoException;
import com.example.task.model.Customer;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerDao extends AbstractDao<Customer, Integer> {

    public long countAll() {
        try {
            String hql = "SELECT COUNT(c) FROM Customer c";
            Query<Long> query = getCurrentSession().createQuery(hql, Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Customer counting error", e);
        }
    }

    public List<Customer> findAllSortedByName() {
        String hql = "FROM Customer c ORDER BY c.name";
        return executeQuery(hql);
    }

    public void updateName(Integer id, String newName) {
        try {
            String hql = "UPDATE Customer c SET c.name = :name WHERE c.id = :id";
            Query<?> query = getCurrentSession().createQuery(hql);
            query.setParameter("name", newName);
            query.setParameter("id", id);
            query.executeUpdate();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error updating customer name", e);
        }
    }

    public boolean existsByName(String name) {
        try {
            String hql = "SELECT COUNT(c) FROM Customer c WHERE LOWER(c.name) = LOWER(:name)";
            Query<Long> query = getCurrentSession().createQuery(hql, Long.class);
            query.setParameter("name", name);
            return query.uniqueResult() > 0;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error checking existence of customer by name", e);
        }
    }
}