package org.example.database;

import org.example.utils.ClassFinder;
import org.example.annotations.TableEntity;
import org.example.utils.ColumnConverter;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QueryBuilder {
    private final static QueryBuilder instance = new QueryBuilder();
    private org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(QueryBuilder.class);
    private Connector connector = Connector.getInstance();

    public static QueryBuilder getInstance() {
        return instance;
    }

    public Table objectToTable(Identifiable object) {

        Class<?> clazz = object.getClass();
        String tableName = clazz.getSimpleName();  // Using class name as table name

        List<String> fieldNames = new ArrayList<>();
        List<Object> fieldValues = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);  // Make private fields accessible
            try {
                logger.info("Field name: " + ColumnConverter.memberToColumn(field.getName()) + " Field value: " + field.get(object));
                fieldNames.add(ColumnConverter.memberToColumn(field.getName()));
                fieldValues.add(field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        Table table = new Table(fieldNames, fieldValues, tableName);
        return table;
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

    private String buildDeleteStatement(String tableName, String idField, Object idValue) {
        String statement = "DELETE FROM " + tableName + " WHERE " + idField + " = '" + idValue.toString().replace("'", "''") + "';";
        logger.info("Generated delete statement: " + statement);
        return statement;
    }

    private String buildUpdateStatement(String tableName, List<String> fieldNames, List<Object> fieldValues, String idField, Object idValue) {
        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for (int i = 0; i < fieldNames.size(); i++) {
            if (fieldValues.get(i) == null || fieldValues.get(i).toString().equals(" ")) {
                continue;
            }

            fields.append(fieldNames.get(i)).append(", ");
            values.append("'").append(fieldValues.get(i).toString().replace("'", "''")).append("', ");
        }

        // Correctly remove the last comma and space if necessary
        if (fields.length() > 0) {
            fields.setLength(fields.length() - 2); // removes the last ", "
        }
        if (values.length() > 0) {
            values.setLength(values.length() - 2); // removes the last ", "
        }

        String statement = "UPDATE " + tableName + " SET " + fields.toString() + " WHERE " + idField + " = '" + idValue.toString().replace("'", "''") + "';";
        logger.info("Generated update statement: " + statement);
        return statement;
    }

    private String buildInsertStatement(String tableName, List<String> fieldNames, List<Object> fieldValues) {
        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for (int i = 0; i < fieldNames.size(); i++) {
            if (fieldValues.get(i) == null || fieldValues.get(i).toString().equals(" ")) {
                continue;
            }

            fields.append(fieldNames.get(i)).append(", ");
            values.append("'").append(fieldValues.get(i).toString().replace("'", "''")).append("', ");
        }

        // Correctly remove the last comma and space if necessary
        if (fields.length() > 0) {
            fields.setLength(fields.length() - 2); // removes the last ", "
        }
        if (values.length() > 0) {
            values.setLength(values.length() - 2); // removes the last ", "
        }

        String statement = "INSERT INTO " + tableName + " (" + fields.toString() + ") VALUES (" + values.toString() + ");";
        logger.info("Generated insert statement: " + statement);
        return statement;
    }

    private String buildSelectStatement(String tableName, String idField, Object idValue) {
        String statement = "SELECT * FROM " + tableName + " WHERE " + idField + " = '" + idValue.toString().replace("'", "''") + "';";
        logger.info("Generated select statement: " + statement);
        return statement;
    }

    public long insert(Table table) {
        String className = ClassFinder.findClassName(table.getName());

        try {
            Class<?> clazz = Class.forName(className);
            TableEntity tableEntity = clazz.getAnnotation(TableEntity.class);
            String statement = buildInsertStatement(tableEntity.tableName(), table.getFields(), table.getValues());
            long id = connector.executeUpdate(statement);
            logger.info("Inserted object into table: " + table.getName() + "ID: " + id);

            return id;
        } catch (ClassNotFoundException e) {
            logger.error("Error inserting object into table: " + table.getName());
            e.printStackTrace();
        }
        return -1;
    }

    public long delete(Table table) {
        String className = ClassFinder.findClassName(table.getName());
        try {
            Class<?> clazz = Class.forName(className);
            TableEntity tableEntity = clazz.getAnnotation(TableEntity.class);
            String statement = buildDeleteStatement(tableEntity.tableName(), table.getFields().get(0), table.getValues().get(0));
            long id = connector.executeUpdate(statement);
            logger.info("Deleted object from table: " + table.getName() + "ID: " + id);

            return id;
        } catch (ClassNotFoundException e) {
            logger.error("Error deleting object from table: " + table.getName());
            e.printStackTrace();
        }
        return -1;
    }

    public long update(Table table) {
        String className = table.getName();
        try {
            Class<?> clazz = Class.forName(className);
            TableEntity tableEntity = clazz.getAnnotation(TableEntity.class);
            String statement = buildUpdateStatement(tableEntity.tableName(), table.getFields(), table.getValues(), table.getFields().get(0), table.getValues().get(0));
            long id = connector.executeUpdate(statement);
            logger.info("Updated object in table: " + table.getName() + "ID: " + id);

            return id;
        } catch (ClassNotFoundException e) {
            logger.error("Error updating object in table: " + table.getName());
            e.printStackTrace();
        }
        return -1;
    }

    public <T> T find(Table table, Class<T> clazz) {
        String className = ClassFinder.findClassName(table.getName());
        try {
            Class<?> tableClass = Class.forName(className);
            TableEntity tableEntity = tableClass.getAnnotation(TableEntity.class);
            String statement = buildSelectStatement(tableEntity.tableName(), table.getFields().get(0), table.getValues().get(0));
            List<T> results = connector.executeQuery(statement, clazz);
            logger.info("Found object in table: " + table.getName());

            return results.get(0);
        } catch (ClassNotFoundException e) {
            logger.error("Error finding object in table: " + table.getName());
            e.printStackTrace();
        }
        return null;
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
            e.printStackTrace();
        }
        return null;
    }
}
