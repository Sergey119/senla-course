package com.example.task.dao.hibernate;

import com.example.task.dao.GenericDAO;
import com.example.task.exception.DaoException;
import com.example.task.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.FetchNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHibernateDAO<T, PK extends Serializable> implements GenericDAO<T, PK> {

    protected static final Logger logger = LogManager.getLogger(AbstractHibernateDAO.class);

    private final Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public AbstractHibernateDAO() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public PK save(T entity) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.openSession();
            transaction = session.beginTransaction();

            PK id = (PK) session.save(entity);
            transaction.commit();
            return id;

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error(e.getMessage());
            throw new DaoException("Error saving entity", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void update(T entity) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.openSession();
            transaction = session.beginTransaction();

            session.update(entity);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error(e.getMessage());
            throw new DaoException("Error updating entity", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void delete(PK id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.openSession();
            transaction = session.beginTransaction();

            T entity = session.get(persistentClass, id);
            if (entity != null) {
                session.delete(entity);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error(e.getMessage());
            throw new DaoException("Error deleting entity with id: " + id, e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public T findById(PK id) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            return session.get(persistentClass, id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error searching for entity with id: " + id, e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public List<T> findAll() {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            String hql = "FROM " + persistentClass.getSimpleName();
            Query<T> query = session.createQuery(hql, persistentClass);
            return query.list();
        } catch (FetchNotFoundException e) {
            // Обработка ситуации, когда связанная сущность не найдена
            logger.error(e.getMessage());
            System.err.println("FetchNotFoundException: " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Ошибка получения всех сущностей", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public boolean existsById(PK id) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            String hql = "SELECT COUNT(e) FROM " + persistentClass.getSimpleName() + " e WHERE e.id = :id";
            Query<Integer> query = session.createQuery(hql, Integer.class);
            query.setParameter("id", id);
            Integer count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Ошибка проверки существования сущности", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    protected List<T> executeQuery(String hql, Object... params) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<T> query = session.createQuery(hql, persistentClass);
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
            return query.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("HQL query execution error: " + hql, e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

}