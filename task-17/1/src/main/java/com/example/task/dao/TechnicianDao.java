package com.example.task.dao;

import com.example.task.exception.DaoException;
import com.example.task.model.Technician;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TechnicianDao extends AbstractDao<Technician, Integer> {

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
        try {
            String hql = "UPDATE Technician t SET t.isAvailable = :available WHERE t.id = :id";
            Query<?> query = getCurrentSession().createQuery(hql);
            query.setParameter("available", isAvailable);
            query.setParameter("id", technicianId);
            int updated = query.executeUpdate();
            logger.info("Updated availability for technician {}: {} ({} rows)", technicianId, isAvailable, updated);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Technician availability status update error", e);
        }
    }

    public void updateSpecialization(Integer technicianId, String newSpecialization) {
        try {
            String hql = "UPDATE Technician t SET t.specialization = :spec WHERE t.id = :id";
            Query<?> query = getCurrentSession().createQuery(hql);
            query.setParameter("spec", newSpecialization);
            query.setParameter("id", technicianId);
            int updated = query.executeUpdate();
            logger.info("Updated specialization for technician {}: {} ({} rows)", technicianId, newSpecialization, updated);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error updating technician specialization", e);
        }
    }
}