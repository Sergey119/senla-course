package com.example.task.dao;

import com.example.task.model.Customer;

import java.sql.*;

public class CustomerDAO extends AbstractJDBCDAO<Customer> {

    private static final String tableName = "customer";
    private static final String idColumn = "id";

    public CustomerDAO() {
        super(tableName, idColumn);
    }

    @Override
    protected Customer mapResultSetToEntity(ResultSet rs) throws SQLException {
        var customer = new Customer();

        customer.setId(rs.getInt("id"));
        customer.setName(rs.getString("name"));

        return customer;
    }

    @Override
    protected void setPreparedStatementForInsert(PreparedStatement ps, Customer entity) throws SQLException {
        ps.setInt(1, entity.getId());
        ps.setString(2, entity.getName());
    }

    @Override
    protected void setPreparedStatementForUpdate(PreparedStatement ps, Customer entity) throws SQLException {
        ps.setString(1, entity.getName());
        ps.setInt(2, entity.getId());
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO customer (id, name) VALUES (?, ?)";
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE customer SET name = ? WHERE id = ?";
    }
}