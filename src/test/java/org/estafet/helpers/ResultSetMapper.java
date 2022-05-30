package org.estafet.helpers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.apache.commons.beanutils.BeanUtils;

public class ResultSetMapper<T> {
    @SuppressWarnings("unchecked")
    public List<T> mapResultSetToObject(ResultSet rs, Class outputClass) {
        List<T> outputList = new ArrayList<>();
        try {
            // make sure resultset is not null
            if (rs != null) {
                // check if outputClass has 'Entity' annotation
                if (outputClass.isAnnotationPresent(Entity.class)) {
                    // get the resultset metadata
                    ResultSetMetaData resultSetMetaData = rs.getMetaData();
                    // get all the attributes of outputClass
                    Field[] fields = outputClass.getDeclaredFields();
                    while (rs.next()) {
                        T bean = null;
                        try {
                            bean = (T) outputClass.getDeclaredConstructor().newInstance();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                            // getting the SQL column name
                            String columnName = resultSetMetaData.getColumnName(i + 1);
                            // reading the value of the SQL column
                            Object columnValue = rs.getObject(i + 1);
                            // iterating over outputClass attributes to check if any attribute has 'Column' annotation with matching 'name' value
                            for (Field field : fields) {
                                if (field.isAnnotationPresent(Column.class)) {
                                    Column column = field.getAnnotation(Column.class);
                                    if (column.name().equalsIgnoreCase(columnName) && columnValue != null) {
                                        BeanUtils.setProperty(bean, field.getName(), columnValue);
                                        break;
                                    }
                                }
                            }
                        }
                        outputList.add(bean);
                    }
                } else {
                    try {
                        throw new Exception("Entity annotation is not added");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                return null;
            }
        } catch (IllegalAccessException | SQLException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return outputList;
    }
}
