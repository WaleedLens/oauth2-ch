package org.example.database;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.AppConfig;
import org.example.exception.DatabaseException;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Connector {
    private static final Connector CONNECTOR = new Connector();
    private final HikariDataSource dataSource;
    org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Connector.class);

    private Connector() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(AppConfig.getDbHost());
        config.setUsername(AppConfig.getDbUsername());
        config.setPassword(AppConfig.getDbPassword());
        config.setDriverClassName("org.postgresql.Driver");
        dataSource = new HikariDataSource(config);
    }

    public static Connector getInstance() {
        return CONNECTOR;
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public <T> List<T> executeQuery(String query, Class<T> clazz, Object... params) {
        List<T> results = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                T object = QueryBuilder.getInstance().resultSetToObject(resultSet, clazz);
                results.add(object);
            }
            logger.info(results.size() + " rows returned.");
            return results;
        } catch (SQLException | IllegalAccessException | InstantiationException | NoSuchFieldException e) {
            logger.error("Error executing query: " + query, e);
            throw new DatabaseException(e);
        }
    }

    public long executeUpdateWithGeneratedKey(String query, Object... params) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                logger.error("Executing update failed, no rows affected.");
                throw new DatabaseException("Executing update failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Executing update failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.error("Error executing update: " + query, e);
            throw new DatabaseException(e);
        }
    }

    public int executeUpdate(String query, Object... params) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                logger.error("Executing update failed, no rows affected.");
                throw new DatabaseException("Executing update failed, no rows affected.");
            }

            return affectedRows;
        } catch (SQLException e) {
            logger.error("Error executing update: " + query, e);
            throw new DatabaseException(e);
        }
    }

}
