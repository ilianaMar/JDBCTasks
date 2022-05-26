package org.estafet.helpers;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DatabaseDriver {

    protected <T> List<T> getDbTableData(Connection connection, String sql, Class<T>  classType) throws SQLException{
        List entityRows;
        QueryRunner queryRunner = new QueryRunner();
        entityRows = queryRunner.query(connection, sql, new BeanListHandler<>(classType));
        return entityRows;
    }


    protected <T> List<T> getDbResultSet(Connection connection, String sql, Class<T>  classType) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();
        ResultSetMapper<T> resultSetMapper = new ResultSetMapper<>();
        List<T> list = resultSetMapper.mapResultSetToObject(resultSet, classType);
        return list;
    }

    protected int insertDbTableRowData(Connection connection, PreparedStatement sql) throws SQLException {
        ScalarHandler<Integer> scalarHandler = new ScalarHandler<>();
        QueryRunner queryRunner = new QueryRunner();
        return queryRunner.insert(connection, String.valueOf(sql), scalarHandler);
    }

    protected void deleteTableData(Connection connection, String sql) throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
        queryRunner.update(connection, sql);
    }
}
