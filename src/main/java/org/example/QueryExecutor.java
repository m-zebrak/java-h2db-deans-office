package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryExecutor {
    private static Connection myConn = DbConnector.getConnection();
    private static Statement myStmt = null;

    public static ResultSet executeQuery(String selectQuery) {
        try {
            myStmt = myConn.createStatement();

            System.out.println("--SELECT QUERY COMPLETED SUCCESSFULLY!--");
            System.out.println(selectQuery);
            System.out.println();

            return myStmt.executeQuery(selectQuery);
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }


    public static void executeUpdate(String updateQuery) {
        try {
            myStmt = myConn.createStatement();
            myStmt.executeUpdate(updateQuery);

            System.out.println("--UPDATE QUERY COMPLETED SUCCESSFULLY!--");
            System.out.println(updateQuery);
            System.out.println();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }


    public static void closeStatement() {
        try {
            myStmt.close();
            System.out.println("--STATEMENT CLOSED--");
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
