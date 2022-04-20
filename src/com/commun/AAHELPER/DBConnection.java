package com.commun.AAHELPER;

import java.sql.*;

public class DBConnection {

    private static String url = "jdbc:mysql://localhost/commun";
    private static final String user = "root";
    private static final String password = "0000";

    public DBConnection() {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection createInstance(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }







































}
