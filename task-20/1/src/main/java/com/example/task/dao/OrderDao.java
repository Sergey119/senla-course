package com.example.task.dao;

import com.example.task.exception.DaoException;
import com.example.task.model.*;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao<Order, Integer> {

    @Override
    public Order findById(Integer id) {
        try {
            Order order = getCurrentSession().get(Order.class, id);
            if (order != null) {
                initializeLazyCollections(order);
            }
            return order;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error finding order by id: " + id, e);
        }
    }

    @Override
    public List<Order> findAll() {
        try {
            String hql = "FROM Order o ORDER BY o.id";
            Query<Order> query = getCurrentSession().createQuery(hql, Order.class);
            List<Order> orders = query.list();

            for (Order order : orders) {
                initializeLazyCollections(order);
            }

            return orders;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error finding all orders", e);
        }
    }

    public void saveOrderWithTechnicians(Order order, List<Integer> technicianIds) {
        try {
            List<Technician> technicians = technicianIds.stream()
                    .map(id -> getCurrentSession().get(Technician.class, id))
                    .filter(t -> t != null)
                    .toList();

            order.setTechnicians(technicians);
            getCurrentSession().save(order);

            logger.info("Order saved with ID: {} and {} technicians", order.getId(), technicians.size());

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error saving order with technicians", e);
        }
    }

    public void addTechnicianToOrder(Integer orderId, Integer technicianId) {
        try {
            Order order = getCurrentSession().get(Order.class, orderId);
            Technician technician = getCurrentSession().get(Technician.class, technicianId);

            if (order != null && technician != null) {
                if (order.getTechnicians() == null) {
                    order.setTechnicians(new java.util.ArrayList<>());
                }
                if (technician.getOrders() == null) {
                    technician.setOrders(new java.util.ArrayList<>());
                }

                order.getTechnicians().add(technician);
                technician.getOrders().add(order);

                getCurrentSession().update(order);
                getCurrentSession().update(technician);

                logger.info("Technician {} added to order {}", technicianId, orderId);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error adding technician to order", e);
        }
    }

    public void removeTechnicianFromOrder(Integer orderId, Integer technicianId) {
        try {
            Order order = getCurrentSession().get(Order.class, orderId);
            Technician technician = getCurrentSession().get(Technician.class, technicianId);

            if (order != null && technician != null && order.getTechnicians() != null) {
                order.getTechnicians().remove(technician);
                technician.getOrders().remove(order);

                getCurrentSession().update(order);
                getCurrentSession().update(technician);

                logger.info("Technician {} removed from order {}", technicianId, orderId);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error removing technician from order", e);
        }
    }

    public List<Long> findTechnicianIdsByOrderId(Integer orderId) {
        try {
            String hql = "SELECT t.id FROM Order o JOIN o.technicians t WHERE o.id = :orderId";
            Query<Long> query = getCurrentSession().createQuery(hql, Long.class);
            query.setParameter("orderId", orderId);
            return query.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error finding technician IDs for order: " + orderId, e);
        }
    }

    public void deleteOrderWithRelations(Integer orderId) {
        try {
            Order order = getCurrentSession().get(Order.class, orderId);
            if (order != null) {
                // Удаляем связи с техниками
                if (order.getTechnicians() != null) {
                    for (Technician technician : order.getTechnicians()) {
                        if (technician.getOrders() != null) {
                            technician.getOrders().remove(order);
                            getCurrentSession().update(technician);
                        }
                    }
                    order.getTechnicians().clear();
                }
                getCurrentSession().delete(order);
                logger.info("Order {} deleted with relations", orderId);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error deleting order with relations", e);
        }
    }

    public List<Order> findByStatus(OrderStatus status) {
        try {
            String hql = "FROM Order o WHERE o.status = :status ORDER BY o.id";
            Query<Order> query = getCurrentSession().createQuery(hql, Order.class);
            query.setParameter("status", status);
            List<Order> orders = query.list();

            for (Order order : orders) {
                initializeLazyCollections(order);
            }
            return orders;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error finding orders by status: " + status, e);
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
        try {
            String hql = "FROM Order o ORDER BY o." + fieldName + (ascending ? " ASC" : " DESC");
            Query<Order> query = getCurrentSession().createQuery(hql, Order.class);
            List<Order> orders = query.list();

            for (Order order : orders) {
                initializeLazyCollections(order);
            }
            return orders;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error finding orders sorted by " + fieldName, e);
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
        try {
            String hql = "FROM Order o WHERE o.status = :status ORDER BY o." + fieldName;
            Query<Order> query = getCurrentSession().createQuery(hql, Order.class);
            query.setParameter("status", status);
            List<Order> orders = query.list();

            for (Order order : orders) {
                initializeLazyCollections(order);
            }
            return orders;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error finding " + status + " orders sorted by " + fieldName, e);
        }
    }

    public List<Order> findOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate, OrderStatus status) {
        try {
            String hql = "FROM Order o WHERE o.startDate >= :startDate AND o.endDate <= :endDate " +
                    "AND o.status = :status ORDER BY o.startDate";
            Query<Order> query = getCurrentSession().createQuery(hql, Order.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            query.setParameter("status", status);
            List<Order> orders = query.list();

            for (Order order : orders) {
                initializeLazyCollections(order);
            }
            return orders;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error finding orders by date range", e);
        }
    }

    public void updateOrderStatus(Integer orderId, OrderStatus newStatus) {
        try {
            Order order = getCurrentSession().get(Order.class, orderId);
            if (order != null) {
                order.setStatus(newStatus);
                getCurrentSession().update(order);
                logger.info("Order {} status updated to {}", orderId, newStatus);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error updating order status", e);
        }
    }

    public void shiftOrderTime(Integer orderId, int hoursToShift) {
        try {
            String sql = "UPDATE orders SET end_date = end_date + :hours * INTERVAL '2 hour' WHERE id = :orderId";
            Query<?> query = getCurrentSession().createNativeQuery(sql);
            query.setParameter("hours", hoursToShift);
            query.setParameter("orderId", orderId);
            query.executeUpdate();
            logger.info("Order {} time shifted by {} hours", orderId, hoursToShift * 2);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error shifting order time", e);
        }
    }

    public List<Order> findOrdersByTechnicianId(Integer technicianId) {
        try {
            String hql = "SELECT o FROM Order o " +
                    "JOIN o.technicians t " +
                    "WHERE t.id = :technicianId " +
                    "ORDER BY o.id";
            Query<Order> query = getCurrentSession().createQuery(hql, Order.class);
            query.setParameter("technicianId", technicianId);
            List<Order> orders = query.list();

            for (Order order : orders) {
                initializeLazyCollections(order);
            }
            return orders;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DaoException("Error finding orders by technician ID: " + technicianId, e);
        }
    }

    private void initializeLazyCollections(Order order) {
        if (order != null) {
            if (order.getTechnicians() != null) {
                order.getTechnicians().size();
            }
            if (order.getServiceAdvisor() != null) {
                getCurrentSession().refresh(order.getServiceAdvisor());
            }
            if (order.getCustomer() != null) {
                getCurrentSession().refresh(order.getCustomer());
            }
            if (order.getCarPlace() != null) {
                getCurrentSession().refresh(order.getCarPlace());
            }
        }
    }
}