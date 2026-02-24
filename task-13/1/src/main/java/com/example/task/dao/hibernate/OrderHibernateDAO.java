package com.example.task.dao.hibernate;

import com.example.task.exception.DaoException;
import com.example.task.model.*;
import com.example.task.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderHibernateDAO extends AbstractHibernateDAO<Order, Integer> {

    public OrderHibernateDAO() {
        super();
    }

    @Override
    public Order findById(Integer id) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Order order = session.get(Order.class, id);
            if (order != null) {
                // Инициализация ленивые коллекции
                initializeLazyCollections(order, session);
            }
            return order;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error finding order by id: " + id, e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> findAll() {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            String hql = "FROM Order o ORDER BY o.id";
            Query<Order> query = session.createQuery(hql, Order.class);
            List<Order> orders = query.list();

            // Инициализация ленивых коллекций для всех заказов
            for (Order order : orders) {
                initializeLazyCollections(order, session);
            }

            return orders;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error finding all orders", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Специфичные методы для Order

    public void saveOrderWithTechnicians(Order order, List<Integer> technicianIds) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();

            List<Technician> technicians = new ArrayList<>();
            for (Integer techId : technicianIds) {
                Technician technician = session.get(Technician.class, techId);
                if (technician != null) {
                    technicians.add(technician);
                }
            }

            order.setTechnicians(technicians);
            session.save(order);
            System.out.println("Order and technicians saved.");

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error saving order with technicians", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void addTechnicianToOrder(Integer orderId, Integer technicianId) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();

            Order order = session.get(Order.class, orderId);
            Technician technician = session.get(Technician.class, technicianId);

            if (order != null && technician != null) {
                // Добавляем техника в заказ и заказ к технику

                order.setTechnicians(new ArrayList<>());
                order.getTechnicians().add(technician);

                technician.setOrders(new ArrayList<>());
                technician.getOrders().add(order);

                session.update(order);
                session.update(technician);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error adding technician to order", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void removeTechnicianFromOrder(Integer orderId, Integer technicianId) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();

            Order order = session.get(Order.class, orderId);
            Technician technician = session.get(Technician.class, technicianId);

            if (order != null && technician != null && order.getTechnicians() != null) {
                // Удаляем техника из заказа и заказ у техника
                order.getTechnicians().remove(technician);
                technician.getOrders().remove(order);

                session.update(order);
                session.update(technician);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error removing technician from order", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<Integer> findTechnicianIdsByOrderId(Integer orderId) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();

            String hql = "SELECT t.id FROM Order o JOIN o.technicians t WHERE o.id = :orderId";
            Query<Integer> query = session.createQuery(hql, Integer.class);
            query.setParameter("orderId", orderId);

            return query.list();

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error finding technician IDs for order: " + orderId, e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void deleteOrderWithRelations(Integer orderId) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();

            Order order = session.get(Order.class, orderId);
            if (order != null) {
                // Перед удалением заказа удаляется связь с техниками
                if (order.getTechnicians() != null) {
                    for (Technician technician : order.getTechnicians()) {
                        if (technician.getOrders() != null) {
                            technician.getOrders().remove(order);
                        }
                    }
                    order.getTechnicians().clear();
                }

                session.delete(order);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error deleting order with relations", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<Order> findByStatus(OrderStatus status) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            String hql = "FROM Order o WHERE o.status = :status ORDER BY o.id";
            Query<Order> query = session.createQuery(hql, Order.class);
            query.setParameter("status", status);
            List<Order> orders = query.list();

            for (Order order : orders) {
                initializeLazyCollections(order, session);
            }

            return orders;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error finding orders by status: " + status, e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<Order> findAllSortedByStartDate() {
        return findAllSorted("startDate", true);
    }

    public List<Order> findAllSortedByEndDate() {
        return findAllSorted("endDate", true);
    }

    public List<Order> findAllSortedByLoadingDate() {
        return findAllSorted("loadingDate", true);
    }

    public List<Order> findAllSortedByCost() {
        return findAllSorted("cost", true);
    }

    public List<Order> findAllSortedByCreatedDate() {
        return findAllSorted("createdDate", true);
    }

    private List<Order> findAllSorted(String fieldName, boolean ascending) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            String hql = "FROM Order o ORDER BY o." + fieldName + (ascending ? " ASC" : " DESC");
            Query<Order> query = session.createQuery(hql, Order.class);
            List<Order> orders = query.list();

            for (Order order : orders) {
                initializeLazyCollections(order, session);
            }

            return orders;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error finding orders sorted by " + fieldName, e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<Order> findCurrentlyRunningOrdersSortedByStartDate() {
        return findOrdersByStatusSorted(OrderStatus.IN_PROGRESS, "startDate");
    }

    public List<Order> findCurrentlyRunningOrdersSortedByEndDate() {
        return findOrdersByStatusSorted(OrderStatus.IN_PROGRESS, "endDate");
    }

    public List<Order> findCurrentlyRunningOrdersSortedByCost() {
        return findOrdersByStatusSorted(OrderStatus.IN_PROGRESS, "cost");
    }

    private List<Order> findOrdersByStatusSorted(OrderStatus status, String fieldName) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            String hql = "FROM Order o WHERE o.status = :status ORDER BY o." + fieldName;
            Query<Order> query = session.createQuery(hql, Order.class);
            query.setParameter("status", status);
            List<Order> orders = query.list();

            for (Order order : orders) {
                initializeLazyCollections(order, session);
            }

            return orders;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error finding " + status + " orders sorted by " + fieldName, e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<Order> findOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate, OrderStatus status) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            String hql = "FROM Order o WHERE o.startDate >= :startDate AND o.endDate <= :endDate " +
                    "AND o.status = :status ORDER BY o.startDate";
            Query<Order> query = session.createQuery(hql, Order.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            query.setParameter("status", status);
            List<Order> orders = query.list();

            for (Order order : orders) {
                initializeLazyCollections(order, session);
            }

            return orders;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error finding orders by date range", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void updateOrderStatus(Integer orderId, OrderStatus newStatus) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.openSession();
            transaction = session.beginTransaction();

            Order order = session.get(Order.class, orderId);
            if (order != null) {
                order.setStatus(newStatus);
                session.update(order);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error(e.getMessage());
            throw new DaoException("Error updating order status", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void shiftOrderTime(Integer orderId, int hoursToShift) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.openSession();
            transaction = session.beginTransaction();

            String sql = "UPDATE orders SET end_date = end_date + :hours * INTERVAL '2 hour' WHERE id = :orderId";
            Query<?> query = session.createNativeQuery(sql);
            query.setParameter("hours", hoursToShift);
            query.setParameter("orderId", orderId);
            query.executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error(e.getMessage());
            throw new DaoException("Error shifting order time", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<Order> findOrdersByTechnicianId(Integer technicianId) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            String hql = "SELECT o FROM Order o " +
                    "JOIN o.technicians t " +
                    "WHERE t.id = :technicianId " +
                    "ORDER BY o.id";
            Query<Order> query = session.createQuery(hql, Order.class);
            query.setParameter("technicianId", technicianId);
            List<Order> orders = query.list();

            for (Order order : orders) {
                initializeLazyCollections(order, session);
            }

            return orders;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error finding orders by technician ID: " + technicianId, e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Вспомогательный метод для инициализации ленивых коллекций
    private void initializeLazyCollections(Order order, Session session) {
        if (order != null && order.getTechnicians() != null) {
            order.getTechnicians().size();

            session.refresh(order.getServiceAdvisor());
            session.refresh(order.getCustomer());
            session.refresh(order.getCarPlace());
        }
    }
}