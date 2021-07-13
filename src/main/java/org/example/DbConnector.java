package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {
    private static String URL = "jdbc:h2:./deans-office";
    private static String USER = "root";
    private static String PASS = "root";

    private static Connection myConn = null;

    public static void createTables() {
        String sql1 = "CREATE TABLE IF NOT EXISTS students (id INT IDENTITY NOT NULL PRIMARY KEY, index_number INT, name VARCHAR(50), surname VARCHAR(50))";
        String sql2 = "CREATE TABLE IF NOT EXISTS subjects (id INT IDENTITY NOT NULL PRIMARY KEY, index_number INT REFERENCES students(index_number), subject VARCHAR(50), rate INT)";
        QueryExecutor.executeUpdate(sql1);
        QueryExecutor.executeUpdate(sql2);
    }

    public static Connection getConnection() {
        /**
         * A function that connects to the DB
         * return: connection to the db or closes the program
         */

        try {
            myConn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("--CONNECTED TO THE DATABASE--\n");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return myConn;
    }

    public static void closeConnection() {
        /**
         * A function that closes connection to the DB
         * return: nothing but closes the program when error occurs
         */
        try {
            myConn.close();
            System.out.println("--THE CONNECTION TO THE DATABASE HAS BEEN CLOSED--!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}