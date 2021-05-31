package org.example;

import java.sql.*;

public class DbConnector {
    private static String URL = "jdbc:mysql://localhost/dziekanat";
    private static String USER = "root";
    private static String PASS = "";

    private static Connection myConn= null;


    public static Connection getConnection() {
        try {
            myConn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("--POLACZONO Z BAZA DANYCH--");
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return myConn;
    }


    public static boolean close() {
        try {
            myConn.close();
            System.out.println("--ZAMKNIETO POŁĄCZENIE Z BAZA DANYCH!");
            return true;
        } catch (SQLException e) {
            e.getMessage();
            return false;
        }
    }
}
