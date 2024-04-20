package org.example.utils;

public class ColumnConverter {

    /**
     * Converts a member in class to a column in the database.
     * @param member
     * @return
     */
    public static String memberToColumn(String member) {

        return member.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    /**
     * Converts a column in the database to a member in class.
     * @param column
     * @return
     */

    public static String columnToMember(String column) {
        String[] parts = column.split("_");
        StringBuilder camelCaseString = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            camelCaseString.append(parts[i].substring(0, 1).toUpperCase()).append(parts[i].substring(1));
        }
        return camelCaseString.toString();
    }

}
