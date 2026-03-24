package com.example.task.dao;

import com.example.task.exception.DataAccessException;
import com.example.task.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractJDBCDAO<T> implements GenericDAO<T> {

    protected final DatabaseConnection dbConnection;
    protected final String tableName;
    protected final String idColumn;

    protected AbstractJDBCDAO(String tableName, String idColumn) {
        this.dbConnection = DatabaseConnection.getInstance();
        this.tableName = tableName;
        this.idColumn = idColumn;
    }

    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
    protected abstract void setPreparedStatementForInsert(PreparedStatement ps, T entity) throws SQLException;
    protected abstract void setPreparedStatementForUpdate(PreparedStatement ps, T entity) throws SQLException;
    protected abstract String getInsertSQL();
    protected abstract String getUpdateSQL();

    @Override
    public Optional<T> findById(Integer id) {
        var sql = "SELECT * FROM " + tableName + " WHERE " + idColumn + " = ?";

        try (var conn = dbConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding entity by id: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        List<T> entities = new ArrayList<>();
        var sql = "SELECT * FROM " + tableName;

        try (var conn = dbConnection.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding all entities", e);
        }
        return entities;
    }

    @Override
    public T save(T entity) {
        var sql = getInsertSQL();

        try (var conn = dbConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {

            setPreparedStatementForInsert(ps, entity);
            ps.executeUpdate();

            return entity;

        } catch (SQLException e) {
            throw new DataAccessException("Error saving entity to table", e);
        }
    }

    @Override
    public T update(T entity) {
        var sql = getUpdateSQL();

        try (var conn = dbConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {

            setPreparedStatementForUpdate(ps, entity);
            ps.executeUpdate();

            return entity;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating entity", e);
        }
    }

    @Override
    public void delete(Integer id) {
        var sql = "DELETE FROM " + tableName + " WHERE " + idColumn + " = ?";

        try (var conn = dbConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error deleting entity with id: " + id, e);
        }
    }

    @Override
    public boolean existsById(Integer id) {
        var sql = "SELECT 1 FROM " + tableName + " WHERE " + idColumn + " = ?";

        try (var conn = dbConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (var rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error checking if entity exists with id: " + id, e);
        }
    }
}
