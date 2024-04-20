package org.example.database;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Connector {
    private final static Connector CONNECTOR = new Connector();
    org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Connector.class);

    public static Connector getInstance() {
        return CONNECTOR;
    }

    private Connection getConnection() throws SQLException {
        String dbUrl = "jdbc:postgresql://localhost:5433/auth_server"; //TODO: remove this line
        String user = "postgres"; // TODO:remove this line
        String password = "123qwe"; // TODO: remove this line
        logger.info("Information: " + dbUrl + " " + user + " " + password); //TODO: remove this line
        logger.info("Connecting to database with url: " + dbUrl);

        return DriverManager.getConnection(dbUrl, user, password);
    }

    public <T> List<T> executeQuery(String query, Class<T> clazz) {
        List<T> results = new ArrayList<>();
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                T object = QueryBuilder.getInstance().resultSetToObject(resultSet, clazz);
                results.add(object);
            }
            logger.info(results.size() + " rows returned.");
            return results;
        } catch (SQLException | IllegalAccessException | InstantiationException | NoSuchFieldException e) {
            logger.error("Error executing query: " + query, e);
            throw new RuntimeException(e);
        }
    }

    public long executeUpdate(String query) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Executing update failed, no rows affected.");
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
            throw new RuntimeException(e);
        }
    }


}
