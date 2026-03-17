package com.example.task.dao;

import com.example.task.exception.DaoException;
import com.example.task.model.CarPlace;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CarPlaceDao extends AbstractDao<CarPlace, Integer> {

    public List<CarPlace> findAvailableCarPlaces() {
        String hql = "FROM CarPlace cp WHERE cp.isOccupied = false";
        return executeQuery(hql);
    }

    public long countAvailableCarPlaces() {
        try {
            String hql = "SELECT COUNT(cp) FROM CarPlace cp WHERE cp.isOccupied = false";
            Query<Long> query = getCurrentSession().createQuery(hql, Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error in counting available car places", e);
        }
    }

    public void updateOccupation(Integer id, boolean isOccupied) {
        try {
            String hql = "UPDATE CarPlace cp SET cp.isOccupied = :occupied WHERE cp.id = :id";
            Query<?> query = getCurrentSession().createQuery(hql);
            query.setParameter("occupied", isOccupied);
            query.setParameter("id", id);
            int updated = query.executeUpdate();
            logger.info("Updated occupation for car place {}: {} ({} rows)", id, isOccupied, updated);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error updating car place status", e);
        }
    }

    public List<CarPlace> findByCarLift(boolean hasCarLift) {
        String hql = "FROM CarPlace cp WHERE cp.carLift = :carLift";
        return executeQuery(hql, hasCarLift);
    }
}