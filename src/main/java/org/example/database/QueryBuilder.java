package org.example.database;

import org.example.exception.DatabaseException;
import org.example.utils.ClassFinder;
import org.example.annotations.TableEntity;
import org.example.utils.ColumnConverter;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class QueryBuilder {
    private final static QueryBuilder instance = new QueryBuilder();
    private org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(QueryBuilder.class);
    private Connector connector = Connector.getInstance();

    public static QueryBuilder getInstance() {
        return instance;
    }

    public Table objectToTable(Identifiable object) {
        // Get the stack trace of the current thread
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String callingMethodName = stackTraceElements[2].getMethodName();
        logger.info("Calling method: {}", callingMethodName);
        Class<?> clazz = object.getClass();
        String tableName = clazz.getSimpleName();  // Using class name as table name


        List<String> fieldNames = new ArrayList<>();
        List<Object> fieldValues = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);  // Make private fields accessible
            String fieldName = field.getName();

            // Skip id, createdAt, and updatedAt fields for insert operations
            if (callingMethodName.equals("save") && (fieldName.equals("id") || fieldName.equals("createdAt") || fieldName.equals("updatedAt"))) {
                continue;
            }

            // Get the value of the field (if it exists)
            try {
                logger.info("Field name: {} Field value: {}", ColumnConverter.memberToColumn(fieldName), field.get(object));
                fieldNames.add(ColumnConverter.memberToColumn(fieldName));
                fieldValues.add(field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return new Table(fieldNames, fieldValues, tableName);
    }

    public <T> T resultSetToObject(ResultSet resultSet, Class<T> clazz) throws SQLException, IllegalAccessException, InstantiationException, NoSuchFieldException, SQLException {
        T instance = clazz.newInstance();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            Object columnValue = resultSet.getObject(i);
            Field field = clazz.getDeclaredField(ColumnConverter.columnToMember(columnName));
            if (field.getType() == Long.class && columnValue instanceof Integer) {
                columnValue = ((Integer) columnValue).longValue();
            }
            field.setAccessible(true);
            field.set(instance, columnValue);
        }

        return instance;
    }


    public long insert(Table table) {
        String className = ClassFinder.findClassName(table.getName());

        try {
            Class<?> clazz = Class.forName(className);
            TableEntity tableEntity = clazz.getAnnotation(TableEntity.class);

            String statement = "INSERT INTO " + tableEntity.tableName() + " (" + String.join(", ", table.getFields()) + ") VALUES (" + generatePlaceholders(table.getValues().size()) + ");";
            long id = connector.executeUpdateWithGeneratedKey(statement, table.getValues().toArray());
            logger.info("Inserted object into table: {}ID: {}", table.getName(), id);

            return id;
        } catch (ClassNotFoundException e) {
            logger.error("Error inserting object into table: {}", table.getName());
            throw new DatabaseException(e);
        }
    }

    public long delete(Table table) {
        String className = ClassFinder.findClassName(table.getName());
        try {
            Class<?> clazz = Class.forName(className);
            TableEntity tableEntity = clazz.getAnnotation(TableEntity.class);
            String statement = "DELETE FROM " + tableEntity.tableName() + " WHERE " + table.getFields().get(0) + " = ?;";
            long id = connector.executeUpdate(statement, table.getValues().get(0));
            logger.info("Deleted object from table: {}ID: {}", table.getName(), id);

            return id;
        } catch (ClassNotFoundException e) {
            logger.error("Error deleting object from table: {}", table.getName());
            throw new DatabaseException(e);
        }
    }

    public long update(Table table) {
        String className = table.getName();
        try {
            Class<?> clazz = Class.forName(className);
            TableEntity tableEntity = clazz.getAnnotation(TableEntity.class);
            String statement = "UPDATE " + tableEntity.tableName() + " SET " + generateSetClause(table.getFields()) + " WHERE " + table.getFields().get(0) + " = ?;";
            List<Object> params = new ArrayList<>(table.getValues());
            params.add(table.getValues().get(0)); // add the ID value at the end
            long id = connector.executeUpdate(statement, params.toArray());
            logger.info("Updated object in table: {}ID: {}", table.getName(), id);

            return id;
        } catch (ClassNotFoundException e) {
            logger.error("Error updating object in table: {}", table.getName());
            throw new DatabaseException(e);
        }
    }

    public <T> T find(Table table, Class<T> clazz) {
        String className = ClassFinder.findClassName(table.getName());
        try {
            Class<?> tableClass = Class.forName(className);
            TableEntity tableEntity = tableClass.getAnnotation(TableEntity.class);
            String statement = "SELECT * FROM " + tableEntity.tableName() + " WHERE " + table.getFields().get(0) + " = ?;";
            List<T> results = connector.executeQuery(statement, clazz, table.getValues().get(0));
            logger.info("Found object in table: {}", table.getName());

            return results.get(0);
        } catch (ClassNotFoundException e) {
            logger.error("Error finding object in table: {}", table.getName());
            throw new DatabaseException(e);
        }
    }

    public <T> T findByField(Table table, Class<T> clazz, String field, Object value) {
        String className = ClassFinder.findClassName(table.getName());
        try {
            Class<?> tableClass = Class.forName(className);
            TableEntity tableEntity = tableClass.getAnnotation(TableEntity.class);
            String statement = "SELECT * FROM " + tableEntity.tableName() + " WHERE " + field + " = ?;";
            List<T> results = connector.executeQuery(statement, clazz, value);
            logger.info("Found object in table: {}", table.getName());

            return results.get(0);
        } catch (ClassNotFoundException e) {
            logger.error("Error finding object in table: {}", table.getName());
            throw new DatabaseException(e);
        }
    }

    public <T> List<T> findAll(Table table, Class<T> clazz) {
        String className = ClassFinder.findClassName(table.getName());
        try {
            Class<?> tableClass = Class.forName(className);
            TableEntity tableEntity = tableClass.getAnnotation(TableEntity.class);
            String statement = "SELECT * FROM " + tableEntity.tableName() + ";";
            List<T> results = connector.executeQuery(statement, clazz);
            logger.info("Found all objects in table: " + table.getName());

            return results;
        } catch (ClassNotFoundException e) {
            logger.error("Error finding all objects in table: " + table.getName());
            throw new DatabaseException(e);
        }
    }

    private String generatePlaceholders(int count) {
        return String.join(", ", Collections.nCopies(count, "?"));
    }

    private String generateSetClause(List<String> fields) {
        return fields.stream().map(field -> field + " = ?").collect(Collectors.joining(", "));
    }
}
