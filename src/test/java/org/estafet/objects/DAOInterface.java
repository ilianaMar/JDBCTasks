package org.estafet.objects;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DAOInterface<Type> {
    int save(Connection dbConnection, Type type) throws SQLException, IOException;
    void delete(Connection dbConnection,int customer_id) throws SQLException;
    void deleteAll(Connection dbConnection) throws SQLException;
    Type getById(Connection dbConnection, int id) throws SQLException;
    Type getByIds(Connection dbConnection, int[] ids) throws SQLException;
    List<?> getAll(Connection dbConnection) throws SQLException;
}


