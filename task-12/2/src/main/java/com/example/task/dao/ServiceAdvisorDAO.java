package com.example.task.dao;

import com.example.task.model.ServiceAdvisor;

import java.sql.*;

public class ServiceAdvisorDAO extends AbstractJDBCDAO<ServiceAdvisor> {

    private static final String tableName = "service_advisor";
    private static final String idColumn = "id";

    public ServiceAdvisorDAO() {
        super(tableName, idColumn);
    }

    @Override
    protected ServiceAdvisor mapResultSetToEntity(ResultSet rs) throws SQLException {
        var serviceAdvisor = new ServiceAdvisor();

        serviceAdvisor.setId(rs.getInt("id"));
        serviceAdvisor.setName(rs.getString("name"));

        return serviceAdvisor;
    }

    @Override
    protected void setPreparedStatementForInsert(PreparedStatement ps, ServiceAdvisor entity) throws SQLException {
        ps.setInt(1, entity.getId());
        ps.setString(2, entity.getName());
    }

    @Override
    protected void setPreparedStatementForUpdate(PreparedStatement ps, ServiceAdvisor entity) throws SQLException {
        ps.setString(1, entity.getName());
        ps.setInt(2, entity.getId());
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO service_advisor (id, name) VALUES (?, ?)";
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE service_advisor SET name = ? WHERE id = ?";
    }
}