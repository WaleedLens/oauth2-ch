package org.example;

public class AppConfig {
    private static final String DB_HOST = System.getenv("db_host");
    private static final String DB_USERNAME = System.getenv("db_username");
    private static final String DB_PASSWORD = System.getenv("db_password");

    public static String getDbHost() {
        return DB_HOST;
    }

    public static String getDbUsername() {
        return DB_USERNAME;
    }

    public static String getDbPassword() {
        return DB_PASSWORD;
    }
}