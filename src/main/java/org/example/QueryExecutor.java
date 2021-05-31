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
            System.out.println("--Zapytanie SELECT QUERY wykonane pomyślnie!--");
            return myStmt.executeQuery(selectQuery);
        } catch (SQLException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }


    public static void execute(String query) {
        try {
            myStmt = myConn.createStatement();
            myStmt.execute(query);
            System.out.println("--Zapytanie QUERY wykonane pomyślnie!--");
        } catch (SQLException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }


    public static void closeStatement() {
        try {
            myStmt.close();
            System.out.println("--ZAMKNIETO STATEMENT--");
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
