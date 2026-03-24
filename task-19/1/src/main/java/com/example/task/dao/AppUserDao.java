package com.example.task.dao;

import com.example.task.exception.DaoException;
import com.example.task.model.AppUser;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class AppUserDao extends AbstractDao<AppUser, Integer> {

    public AppUser findByUsername(String username) {
        try {
            String hql = "FROM AppUser WHERE username = :username";
            Query<AppUser> query = getCurrentSession().createQuery(hql, AppUser.class);
            query.setParameter("username", username);
            return query.setMaxResults(1).uniqueResult();
        } catch (Exception e) {
            logger.error("Error finding user by username: [{}]. {}", username, e);
            throw new DaoException("Error finding user by username: " + username);
        }
    }
}