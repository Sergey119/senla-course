package com.example.task.dao;

import com.example.task.exception.DataAccessException;
import com.example.task.model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAO extends AbstractJDBCDAO<Order> {

    private static final String tableName = "\"order\"";
    private static final String idColumn = "id";

    private static final String insertSql =
            "INSERT INTO \"order\" (id, service_advisor_id, customer_id, car_place_id, status, cost, " +
                    "created_date, start_date, loading_date, end_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String updateSql =
            "UPDATE \"order\" SET service_advisor_id = ?, customer_id = ?, car_place_id = ?, " +
                    "status = ?, cost = ?, created_date = ?, start_date = ?, " +
                    "loading_date = ?, end_date = ? WHERE id = ?";

    private ServiceAdvisorDAO serviceAdvisorDAO;
    private CustomerDAO customerDAO;
    private CarPlaceDAO carPlaceDAO;
    private TechnicianDAO technicianDAO;

    public OrderDAO() {
        super(tableName, idColumn);
    }

    public void setServiceAdvisorDAO(ServiceAdvisorDAO serviceAdvisorDAO) {
        this.serviceAdvisorDAO = serviceAdvisorDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public void setCarPlaceDAO(CarPlaceDAO carPlaceDAO) {
        this.carPlaceDAO = carPlaceDAO;
    }

    public void setTechnicianDAO(TechnicianDAO technicianDAO) {
        this.technicianDAO = technicianDAO;
    }

    @Override
    protected Order mapResultSetToEntity(ResultSet rs) throws SQLException {
        var order = new Order();

        order.setId(rs.getInt("id"));

        String statusStr = rs.getString("status");
        try {
            order.setStatus(OrderStatus.valueOf(statusStr));
        } catch (IllegalArgumentException e) {
            order.setStatus(OrderStatus.PENDING);
        }

        order.setCost(rs.getInt("cost"));

        var createdDate = rs.getTimestamp("created_date");
        order.setCreatedDate(createdDate.toLocalDateTime());

        var startDate = rs.getTimestamp("start_date");
        order.setStartDate(startDate.toLocalDateTime());

        var loadingDate = rs.getTimestamp("loading_date");
        order.setLoadingDate(loadingDate.toLocalDateTime());

        var endDate = rs.getTimestamp("end_date");
        order.setEndDate(endDate.toLocalDateTime());

        int advisorId = rs.getInt("service_advisor_id");
        int customerId = rs.getInt("customer_id");
        int carPlaceId = rs.getInt("car_place_id");

        var advisor = new ServiceAdvisor();
        advisor.setId(advisorId);
        order.setServiceAdvisor(advisor);

        var customer = new Customer();
        customer.setId(customerId);
        order.setCustomer(customer);

        var carPlace = new CarPlace();
        carPlace.setId(carPlaceId);
        order.setCarPlace(carPlace);

        return order;
    }

    private void loadAllRelations(Order order) {

        serviceAdvisorDAO.findById(order.getServiceAdvisor().getId()).ifPresent(order::setServiceAdvisor);

        customerDAO.findById(order.getCustomer().getId()).ifPresent(order::setCustomer);

        carPlaceDAO.findById(order.getCarPlace().getId()).ifPresent(order::setCarPlace);

        var technicianIds = findTechnicianIdsByOrderId(order.getId());
        List<Technician> technicians = new ArrayList<>();
        for (var techId : technicianIds) {
            technicianDAO.findById(techId).ifPresent(technicians::add);
        }
        order.setTechnicians(technicians);
    }

    @Override
    protected void setPreparedStatementForInsert(PreparedStatement ps, Order entity) throws SQLException {
        ps.setInt(1, entity.getId());

        ps.setInt(2, entity.getServiceAdvisor().getId());

        ps.setInt(3, entity.getCustomer().getId());

        ps.setInt(4, entity.getCarPlace().getId());

        ps.setString(5, entity.getStatus().name());

        ps.setInt(6, entity.getCost());

        ps.setTimestamp(7, Timestamp.valueOf(entity.getCreatedDate()));

        ps.setTimestamp(8, Timestamp.valueOf(entity.getCreatedDate()));

        ps.setTimestamp(9, Timestamp.valueOf(entity.getCreatedDate()));

        ps.setTimestamp(10, Timestamp.valueOf(entity.getCreatedDate()));
    }

    @Override
    protected void setPreparedStatementForUpdate(PreparedStatement ps, Order entity) throws SQLException {
        ps.setInt(1, entity.getServiceAdvisor().getId());

        ps.setInt(2, entity.getCustomer().getId());

        ps.setInt(3, entity.getCarPlace().getId());

        ps.setString(4, entity.getStatus().name());

        ps.setInt(5, entity.getCost());

        ps.setTimestamp(6, Timestamp.valueOf(entity.getCreatedDate()));

        ps.setTimestamp(7, Timestamp.valueOf(entity.getCreatedDate()));

        ps.setTimestamp(8, Timestamp.valueOf(entity.getCreatedDate()));

        ps.setTimestamp(9, Timestamp.valueOf(entity.getCreatedDate()));

        ps.setInt(10, entity.getId());
    }

    @Override
    protected String getInsertSQL() {
        return insertSql;
    }

    @Override
    protected String getUpdateSQL() {
        return updateSql;
    }

    public void saveOrderWithTechnicians(Order order, List<Integer> technicianIds) {
        Connection conn = null;
        try {
            conn = dbConnection.getConnectionWithoutAutoCommit();
            insertOrder(conn, order);
            for (var techId : technicianIds) {
                addTechnicianToOrder(conn, order.getId(), techId);
            }
            conn.commit();
            System.out.println("Order and technicians saved atomically.");
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    throw new DataAccessException("Error during rollback", rollbackEx);
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    public void addTechnicianToOrder(Integer orderId, Integer technicianId) {
        var sql = "INSERT INTO order_technician (order_id, technician_id) VALUES (?, ?)";

        try (var conn = dbConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.setInt(2, technicianId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error adding technician to order", e);
        }
    }

    private void addTechnicianToOrder(Connection conn, Integer orderId, Integer technicianId) throws SQLException {
        var sql = "INSERT INTO order_technician (order_id, technician_id) VALUES (?, ?)";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, technicianId);
            ps.executeUpdate();
        }
    }

    public void removeTechnicianFromOrder(Integer orderId, Integer technicianId) {
        var sql = "DELETE FROM order_technician WHERE order_id = ? AND technician_id = ?";

        try (var conn = dbConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.setInt(2, technicianId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error removing technician to order", e);
        }
    }

    public List<Integer> findTechnicianIdsByOrderId(Integer orderId) {
        List<Integer> technicianIds = new ArrayList<>();
        var sql = "SELECT technician_id FROM order_technician WHERE order_id = ?";

        try (var conn = dbConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    technicianIds.add(rs.getInt("technician_id"));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding technician IDs for order: " + orderId, e);
        }
        return technicianIds;
    }

    public Optional<Order> findByIdWithAllRelations(Integer id) {
        var orderOpt = super.findById(id);

        if (orderOpt.isPresent()) {
            var order = orderOpt.get();
            loadAllRelations(order);
            return Optional.of(order);
        }

        return Optional.empty();
    }

    public void deleteOrderWithRelations(Integer orderId) {
        Connection conn = null;
        try {
            conn = dbConnection.getConnectionWithoutAutoCommit();

            var deleteRelationsSql = "DELETE FROM order_technician WHERE order_id = ?";
            try (var ps = conn.prepareStatement(deleteRelationsSql)) {
                ps.setInt(1, orderId);
                ps.executeUpdate();
            }

            var deleteOrderSql = "DELETE FROM \"order\" WHERE id = ?";
            try (var ps = conn.prepareStatement(deleteOrderSql)) {
                ps.setInt(1, orderId);
                ps.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    throw new DataAccessException("Error during rollback", rollbackEx);
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public Optional<Order> findById(Integer id) {
        return findByIdWithAllRelations(id);
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = super.findAll();
        for (Order order : orders) {
            loadAllRelations(order);
        }
        return orders;
    }

    private void insertOrder(Connection conn, Order entity) throws SQLException {
        try (var ps = conn.prepareStatement(insertSql)) {
            ps.setInt(1, entity.getId());
            ps.setInt(2, entity.getServiceAdvisor().getId());
            ps.setInt(3, entity.getCustomer().getId());
            ps.setInt(4, entity.getCarPlace().getId());
            ps.setString(5, entity.getStatus().name());
            ps.setInt(6, entity.getCost());
            ps.setTimestamp(7, Timestamp.valueOf(entity.getCreatedDate()));
            ps.setTimestamp(8, Timestamp.valueOf(entity.getStartDate()));
            ps.setTimestamp(9, Timestamp.valueOf(entity.getLoadingDate()));
            ps.setTimestamp(10, Timestamp.valueOf(entity.getEndDate()));
            ps.executeUpdate();
        }
    }

    // Специфичные методы для Order

    private List<Order> findOrdersByQuery(String sql, Integer param) {
        List<Order> orders = new ArrayList<>();
        try (var conn = dbConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setInt(1, param);

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    var order = mapResultSetToEntity(rs);
                    loadAllRelations(order);
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding orders by query: " + sql, e);
        }
        return orders;
    }

    private List<Order> findOrdersByQuery(String sql, String param) {
        List<Order> orders = new ArrayList<>();
        try (var conn = dbConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setString(1, param);

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    var order = mapResultSetToEntity(rs);
                    loadAllRelations(order);
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding orders by query: " + sql, e);
        }
        return orders;
    }



    public List<Order> findByStatus(OrderStatus status) {
        var sql = "SELECT * FROM \"order\" WHERE status = ? ORDER BY id";
        return findOrdersByQuery(sql, status.name());
    }

    public List<Order> findByCustomerId(Integer customerId) {
        var sql = "SELECT * FROM \"order\" WHERE customer_id = ? ORDER BY id";
        return findOrdersByQuery(sql, customerId);
    }

    public List<Order> findByCarPlaceId(Integer carPlaceId) {
        var sql = "SELECT * FROM \"order\" WHERE car_place_id = ? ORDER BY id";
        return findOrdersByQuery(sql, carPlaceId);
    }

    public List<Order> findByServiceAdvisorId(Integer serviceAdvisorId) {
        var sql = "SELECT * FROM \"order\" WHERE service_advisor_id = ? ORDER BY id";
        return findOrdersByQuery(sql, serviceAdvisorId);
    }

    public List<Order> findAllSortedByStartDate() {
        return findAllSorted("start_date");
    }

    public List<Order> findAllSortedByEndDate() {
        return findAllSorted("end_date");
    }

    public List<Order> findAllSortedByLoadingDate() {
        return findAllSorted("loading_date");
    }

    public List<Order> findAllSortedByCost() {
        return findAllSorted("cost");
    }

    public List<Order> findAllSortedByCreatedDate() {
        return findAllSorted("created_date");
    }

    private List<Order> findAllSorted(String column) {
        List<Order> orders = new ArrayList<>();
        var sql = "SELECT * FROM \"order\" ORDER BY " + column;

        try (var conn = dbConnection.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                var order = mapResultSetToEntity(rs);
                loadAllRelations(order);
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding orders sorted by " + column, e);
        }
        return orders;
    }

    public List<Order> findCurrentlyRunningOrdersSortedByStartDate() {
        return findOrdersByStatusSorted(OrderStatus.IN_PROGRESS, "start_date");
    }

    public List<Order> findCurrentlyRunningOrdersSortedByEndDate() {
        return findOrdersByStatusSorted(OrderStatus.IN_PROGRESS, "end_date");
    }

    public List<Order> findCurrentlyRunningOrdersSortedByCost() {
        return findOrdersByStatusSorted(OrderStatus.IN_PROGRESS, "cost");
    }

    private List<Order> findOrdersByStatusSorted(OrderStatus status, String column) {
        var sql = "SELECT * FROM \"order\" WHERE status = ? ORDER BY " + column;
        return findOrdersByQuery(sql, status.name());
    }

    public List<Order> findOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate, OrderStatus status) {
        var sql = "SELECT * FROM \"order\" WHERE start_date >= ? AND end_date <= ? AND status = ? ORDER BY start_date";

        try (var conn = dbConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));
            ps.setString(3, status.name());

            List<Order> orders = new ArrayList<>();
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    var order = mapResultSetToEntity(rs);
                    loadAllRelations(order);
                    orders.add(order);
                }
            }
            return orders;

        } catch (SQLException e) {
            throw new DataAccessException("Error finding orders by date range", e);
        }
    }

    public void updateOrderStatus(Integer orderId, OrderStatus newStatus) {
        var sql = "UPDATE \"order\" SET status = ? WHERE id = ?";

        try (var conn = dbConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus.name());
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error updating order status", e);
        }
    }

    public void shiftOrderTime(Integer orderId, int hoursToShift) {
        var sql = "UPDATE \"order\" SET end_date = end_date + ? * INTERVAL '2 hour' WHERE id = ?";

        try (var conn = dbConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setInt(1, hoursToShift);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error shifting order time", e);
        }
    }

    public List<Order> findOrdersByTechnicianId(Integer technicianId) {
        var sql = "SELECT o.* FROM \"order\" o " +
                "INNER JOIN order_technician ot ON o.id = ot.order_id " +
                "WHERE ot.technician_id = ? " +
                "ORDER BY o.id";

        List<Order> orders = new ArrayList<>();

        try (var conn = dbConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setInt(1, technicianId);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    var order = mapResultSetToEntity(rs);
                    loadAllRelations(order);
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding orders by technician ID: " + technicianId, e);
        }
        return orders;
    }
}