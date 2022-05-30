package org.estafet.helpers;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DatabaseDriver {

    protected <T> List getDbTableData(Connection connection, String sql, Class<T> classType) throws SQLException {
        List entityRows;
        QueryRunner queryRunner = new QueryRunner();
        entityRows = queryRunner.query(connection, sql, new BeanListHandler<>(classType));
        return entityRows;
    }


    protected <T> List<T> getDbResultSet(Connection connection, String sql, Class<T> classType) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();
        ResultSetMapper<T> resultSetMapper = new ResultSetMapper<>();
        return resultSetMapper.mapResultSetToObject(resultSet, classType);
    }

    protected int insertDbTableRowData(Connection connection, PreparedStatement sql) throws SQLException {
        ScalarHandler<Integer> scalarHandler = new ScalarHandler<>();
        QueryRunner queryRunner = new QueryRunner();
        return queryRunner.insert(connection, String.valueOf(sql), scalarHandler);
    }

    protected long getDbTableCount(Connection connection, String sql) throws SQLException {
        ScalarHandler<Long> scalarHandler = new ScalarHandler<>();
        QueryRunner queryRunner = new QueryRunner();
        return queryRunner.query(connection, sql, scalarHandler);
    }

    protected void deleteTableData(Connection connection, String sql) throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
        queryRunner.update(connection, sql);
    }
}
