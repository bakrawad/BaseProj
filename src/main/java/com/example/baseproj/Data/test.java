package com.example.baseproj.Data;

import java.sql.*;
import java.util.Properties;

public class test {
    private static String dbURL;
    private static String dbUsername = "root";
    private static String dbPassword = "123456";
    private static String URL = "localhost";
    private static String port = "3306";
    private static String dbName = "bakr_and_mohammad";
    private static Connection con;

    private static void connectDB() throws ClassNotFoundException, SQLException {

        dbURL = "jdbc:mysql://" + URL + ":" + port + "/" + dbName + "?verifyServerCertificate=false";
        Properties p = new Properties();
        p.setProperty("user", dbUsername);
        p.setProperty("password", dbPassword);
        p.setProperty("useSSL", "false");
        p.setProperty("autoReconnect", "true");
        Class.forName("com.mysql.cj.jdbc.Driver");

        con = DriverManager.getConnection(dbURL, p);

    }


    public static void  ExecuteStatement(String SQL) throws SQLException, ClassNotFoundException {
        connectDB();
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(SQL);
            stmt.close();

        } catch (SQLException s) {
            s.printStackTrace();
            System.out.println("SQL statement is not executed!");

        }

    }
//    private static void getCustomerData() throws SQLException, ClassNotFoundException {
//
//        String SQL;
//        StringBuilder s = new StringBuilder();
//        connectDB();
//        System.out.println("Connection established");
//
//        SQL = "select CustomerID,CustomerName,CustomerAddress,PhoneNumber from Customers order by CustomerID";
//        Statement stmt = con.createStatement();
//        ResultSet rs = stmt.executeQuery(SQL);
//
//        while (rs.next())
//            s.append(new Customers(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3),
//                    rs.getString(4))+"\n");
//
//        rs.close();
//        stmt.close();
//
//        con.close();
//        System.out.println("Connection closed ");
//        System.out.println(s);
//    }



    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        connectDB();
        //getCustomerData();
        ExecuteStatement("Delete from Customers\n" +
                "where CustomerID = 5;");
        //getCustomerData();

    }

}
