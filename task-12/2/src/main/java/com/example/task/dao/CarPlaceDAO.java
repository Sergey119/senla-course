package com.example.task.dao;

import com.example.task.exception.DataAccessException;
import com.example.task.model.CarPlace;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarPlaceDAO extends AbstractJDBCDAO<CarPlace> {

    private static final String tableName = "car_place";
    private static final String idColumn = "id";

    public CarPlaceDAO() {
        super(tableName, idColumn);
    }

    @Override
    protected CarPlace mapResultSetToEntity(ResultSet rs) throws SQLException {
        var carPlace = new CarPlace();
        carPlace.setId(rs.getInt("id"));
        carPlace.setSquare(rs.getInt("square"));
        carPlace.setCarLift(rs.getBoolean("car_lift"));
        carPlace.setIsOccupied(rs.getBoolean("is_occupied"));
        return carPlace;
    }

    @Override
    protected void setPreparedStatementForInsert(PreparedStatement ps, CarPlace entity) throws SQLException {
        ps.setInt(1, entity.getId());
        ps.setInt(2, entity.getSquare());
        ps.setBoolean(3, entity.getCarLift());
        ps.setBoolean(4, entity.getIsOccupied());
    }

    @Override
    protected void setPreparedStatementForUpdate(PreparedStatement ps, CarPlace entity) throws SQLException {
        ps.setInt(1, entity.getSquare());
        ps.setBoolean(2, entity.getCarLift());
        ps.setBoolean(3, entity.getIsOccupied());
        ps.setInt(4, entity.getId());
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO car_place (id, square, car_lift, is_occupied) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE car_place SET square = ?, car_lift = ?, is_occupied = ? WHERE id = ?";
    }

    // Специфичные методы для CarPlace

    public List<CarPlace> findAvailableCarPlaces() {
        List<CarPlace> carPlaces = new ArrayList<>();
        var sql = "SELECT * FROM car_place WHERE is_occupied = false";

        try (var conn = dbConnection.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                carPlaces.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding available car places", e);
        }
        return carPlaces;
    }

    public int countAvailableCarPlaces() {
        var sql = "SELECT COUNT(*) FROM car_place WHERE is_occupied = false";

        try (var conn = dbConnection.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error counting available car places", e);
        }
    }

    public void updateOccupation(Integer id, boolean isOccupied) {
        var sql = "UPDATE car_place SET is_occupied = ? WHERE id = ?";

        try (var conn = dbConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, isOccupied);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error updating car place occupation status", e);
        }
    }
}