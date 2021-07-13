package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryExecutor {
    private static Connection myConn = DbConnector.getConnection();
    private static Statement myStmt = null;

    public static void createStatement() {
        /**
         * A function that creates a statement
         */
        try {
            myStmt = myConn.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static ResultSet executeSelect(String selectQuery) {
        /**
         * A function that executes SELECT queries
         * return: ResultSet of passed SELECT query if successful
         */
        ResultSet resultSet = null;
        try {
            createStatement();
            resultSet = myStmt.executeQuery(selectQuery);

            System.out.println("--SELECT QUERY COMPLETED SUCCESSFULLY!--");
            System.out.println(selectQuery + "\n");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return resultSet;
    }

    public static void executeUpdate(String updateQuery) {
        /**
         * A function that executes  UPDATE or DELETE queries
         */
        try {
            createStatement();
            myStmt.executeUpdate(updateQuery);

            System.out.println("--UPDATE QUERY COMPLETED SUCCESSFULLY!--");
            System.out.println(updateQuery + "\n");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void closeStatement() {
        /**
         * A function that closes the statement
         */
        try {
            myStmt.close();
            System.out.println("--STATEMENT CLOSED--");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
