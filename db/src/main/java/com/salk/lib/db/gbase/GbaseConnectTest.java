package com.salk.lib.db.gbase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author salkli
 * @since 2022/8/16
 **/
public class GbaseConnectTest {

    public static void main(String[] args) {

        String url = "jdbc:gbase://124.70.165.153:19088/testdb";
        String userName = "gbasedbt";
        String pwd = "GBase123";
        try {
            Class.forName("com.gbase.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, userName, pwd);
            System.out.println(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
