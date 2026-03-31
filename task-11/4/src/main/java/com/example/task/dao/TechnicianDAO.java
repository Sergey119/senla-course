package com.example.task.dao;

import com.example.task.exception.DataAccessException;
import com.example.task.model.Technician;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TechnicianDAO extends AbstractJDBCDAO<Technician> {

    private static final String tableName = "technician";
    private static final String idColumn = "id";

    public TechnicianDAO() {
        super(tableName, idColumn);
    }

    @Override
    protected Technician mapResultSetToEntity(ResultSet rs) throws SQLException {
        var technician = new Technician();

        technician.setId(rs.getInt("id"));
        technician.setName(rs.getString("name"));
        technician.setSpecialization(rs.getString("specialization"));
        technician.setIsAvailable(rs.getBoolean("is_available"));

        return technician;
    }

    @Override
    protected void setPreparedStatementForInsert(PreparedStatement ps, Technician entity) throws SQLException {
        ps.setInt(1, entity.getId());
        ps.setString(2, entity.getName());
        ps.setString(3, entity.getSpecialization());
        ps.setBoolean(4, entity.getIsAvailable());
    }

    @Override
    protected void setPreparedStatementForUpdate(PreparedStatement ps, Technician entity) throws SQLException {
        ps.setString(1, entity.getName());
        ps.setString(2, entity.getSpecialization());
        ps.setBoolean(3, entity.getIsAvailable());
        ps.setInt(4, entity.getId());
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO technician (id, name, specialization, is_available) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE technician SET name = ?, specialization = ?, is_available = ? WHERE id = ?";
    }

    // Специфичные методы для Technician
    public List<Technician> findAvailableTechnicians() {
        List<Technician> technicians = new ArrayList<>();
        var sql = "SELECT * FROM technician WHERE is_available = true";

        try (var conn = dbConnection.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                technicians.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding available technicians", e);
        }
        return technicians;
    }

    public List<Technician> findByOrderId(Integer orderId) {
        List<Technician> technicians = new ArrayList<>();
        var sql = "SELECT t.* FROM technician t " +
                "INNER JOIN order_technician ot ON t.id = ot.technician_id " +
                "WHERE ot.order_id = ?";

        try (var conn = dbConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    technicians.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding technicians by order id: " + orderId, e);
        }
        return technicians;
    }

    public List<Technician> findAllSortedByName() {
        List<Technician> technicians = new ArrayList<>();
        var sql = "SELECT * FROM technician ORDER BY name";

        try (var conn = dbConnection.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                technicians.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding technicians sorted by name", e);
        }
        return technicians;
    }

    public List<Technician> findAllSortedByAvailability() {
        List<Technician> technicians = new ArrayList<>();
        var sql = "SELECT * FROM technician ORDER BY is_available DESC, name";

        try (var conn = dbConnection.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                technicians.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding technicians sorted by availability", e);
        }
        return technicians;
    }
}