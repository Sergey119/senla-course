package com.example.task.dao;

import com.example.task.exception.DaoException;
import com.example.task.model.ServiceAdvisor;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ServiceAdvisorDao extends AbstractDao<ServiceAdvisor, Integer> {

    public long countAll() {
        try {
            String hql = "SELECT COUNT(sa) FROM ServiceAdvisor sa";
            Query<Long> query = getCurrentSession().createQuery(hql, Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Service advisor counting error", e);
        }
    }

    public List<ServiceAdvisor> findAllSortedByName() {
        String hql = "FROM ServiceAdvisor sa ORDER BY sa.name";
        return executeQuery(hql);
    }

    public void updateName(Integer id, String newName) {
        try {
            String hql = "UPDATE ServiceAdvisor sa SET sa.name = :name WHERE sa.id = :id";
            Query<?> query = getCurrentSession().createQuery(hql);
            query.setParameter("name", newName);
            query.setParameter("id", id);
            int updated = query.executeUpdate();
            logger.info("Updated name for service advisor {}: {} ({} rows)", id, newName, updated);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error updating service advisor name", e);
        }
    }

    public boolean existsByName(String name) {
        try {
            String hql = "SELECT COUNT(sa) FROM ServiceAdvisor sa WHERE LOWER(sa.name) = LOWER(:name)";
            Query<Long> query = getCurrentSession().createQuery(hql, Long.class);
            query.setParameter("name", name);
            return query.uniqueResult() > 0;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error checking existence of service advisor by name", e);
        }
    }
}