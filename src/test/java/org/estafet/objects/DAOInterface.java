package org.estafet.objects;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DAOInterface<Type> {
    int save(Connection dbConnection, Type type) throws SQLException;

    void delete(Connection dbConnection, int id) throws SQLException;

    void deleteAll(Connection dbConnection) throws SQLException;

    List<?> getById(Connection dbConnection, int id, String columnName) throws SQLException;

    List<?> getByIds(Connection dbConnection, String columnName, List<Integer> ids) throws SQLException;

    List<?> getAll(Connection dbConnection) throws SQLException;

    int getAllRecordsCount(Connection dbConnection) throws SQLException;

    List<?> getByRandomId(Connection dbConnection) throws SQLException;

    List<?> getRandomIds(Connection dbConnection, int count) throws SQLException;
}