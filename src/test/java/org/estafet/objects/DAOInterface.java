package org.estafet.objects;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DAOInterface<Type> {
    void save(Connection dbConnection, Type type) throws SQLException, IOException;
    void delete(Connection dbConnection,int id) throws SQLException;
    void deleteAll(Connection dbConnection) throws SQLException;
    Type getById(Connection dbConnection, int id) throws SQLException;
    List<?> getByIds(Connection dbConnection, List<Integer> ids) throws SQLException;
    List<?> getAll(Connection dbConnection) throws SQLException;
    int getAllRecordsCount(Connection dbConnection) throws SQLException;
    Type getByRandomId(Connection dbConnection) throws SQLException;
    List<?> getRandomIds(Connection dbConnection, int count) throws SQLException;
}


