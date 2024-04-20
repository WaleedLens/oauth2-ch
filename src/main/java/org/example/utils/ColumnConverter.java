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

}
