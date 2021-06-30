package org.example;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {
    private static String URL = "jdbc:h2:./deans-office";
    private static String USER = "root";
    private static String PASS = "root";

    private static Connection myConn = null;

    public static void createTables(){
        String sql1 = "CREATE TABLE IF NOT EXISTS students (id INT IDENTITY NOT NULL PRIMARY KEY, index_number INT, name VARCHAR(50), surname VARCHAR(50))";
        String sql2 = "CREATE TABLE IF NOT EXISTS subjects (id INT IDENTITY NOT NULL PRIMARY KEY, index_number INT REFERENCES students(index_number), subject VARCHAR(50), rate INT)";
        QueryExecutor.executeUpdate(sql1);
        QueryExecutor.executeUpdate(sql2);
    }

    public static Connection getConnection() {
        try {
            myConn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("--CONNECTED TO THE DATABASE--\n");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return myConn;
    }


    public static boolean close() {
        try {
            myConn.close();
            System.out.println("--THE CONNECTION TO THE DATABASE HAS BEEN CLOSED--!");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
