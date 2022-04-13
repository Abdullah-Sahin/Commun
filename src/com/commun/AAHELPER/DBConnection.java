package com.commun.AAHELPER;

import com.commun.MODELS.Post;
import com.commun.MODELS.User;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {

    private static final String url = "jdbc:mysql://localhost/commun";
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
