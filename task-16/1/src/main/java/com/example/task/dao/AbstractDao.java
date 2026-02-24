package com.example.task.dao;

import com.example.task.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.FetchNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T, PK extends Serializable> implements GenericDao<T, PK> {

    protected static final Logger logger = LogManager.getLogger(AbstractDao.class);

    private final Class<T> persistentClass;

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public AbstractDao() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public PK save(T entity) {
        try {
            return (PK) getCurrentSession().save(entity);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error saving entity", e);
        }
    }

    @Override
    public void update(T entity) {
        try {
            getCurrentSession().update(entity);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error updating entity", e);
        }
    }

    @Override
    public void delete(PK id) {
        try {
            T entity = getCurrentSession().get(persistentClass, id);
            if (entity != null) {
                getCurrentSession().delete(entity);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error deleting entity with id: " + id, e);
        }
    }

    @Override
    public T findById(PK id) {
        try {
            return getCurrentSession().get(persistentClass, id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error searching for entity with id: " + id, e);
        }
    }

    @Override
    public List<T> findAll() {
        try {
            String hql = "FROM " + persistentClass.getSimpleName();
            Query<T> query = getCurrentSession().createQuery(hql, persistentClass);
            return query.list();
        } catch (FetchNotFoundException e) {
            logger.error(e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error getting all entities", e);
        }
    }

    @Override
    public boolean existsById(PK id) {
        try {
            String hql = "SELECT COUNT(e) FROM " + persistentClass.getSimpleName() + " e WHERE e.id = :id";
            Query<Long> query = getCurrentSession().createQuery(hql, Long.class);
            query.setParameter("id", id);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error checking entity existence", e);
        }
    }

    protected List<T> executeQuery(String hql, Object... params) {
        Query<T> query = getCurrentSession().createQuery(hql, persistentClass);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query.list();
    }
}