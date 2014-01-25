/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sheky
 */
public class database {

    public static Connection mysqlConnection = null;
    public static Class theClass = null;
    public static ResultSet resultSet = null;

    public static void dbConnect() {
        if (mysqlConnection == null) {
            readProperties readProp = new readProperties();
            try {                
                String className = readProp.readProp("db.driver");
                String database = "jdbc:mysql://" + readProp.readProp("host") + "/" + readProp.readProp("db.name");
                String user = readProp.readProp("db.user");
                String pass = readProp.readProp("db.pass");
                System.out.println("db:" + database + "  user:" + user + "  pass:" + pass);



                try {
                    theClass = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
                } catch (ClassNotFoundException e) {
                    theClass = Class.forName(className);
                }
                mysqlConnection = DriverManager.getConnection(database, user, pass);
                System.out.println("Uspjesno povezivanje na bazu!");

            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        } else {
            System.out.println("Vec ste spojeni na bazu");
        }
    }

    public static void dbDisconect() {
        try {
            if (mysqlConnection != null) {
                mysqlConnection.close();
                theClass = null;
            }
            System.out.println("Odspajanje sa baze");
        } catch (SQLException ex) {
            System.out.println("Neuspjesno odspajanje od baza");
            ex.printStackTrace();
        }
    }

    public static void connectOnDemand() {
        if (mysqlConnection == null) {
            dbConnect();
        }
    }

    public static void sqlQueryString(String sql) {
        try {
            connectOnDemand(); //spajanje na bazu, ako nije spojen
            Statement stat = mysqlConnection.createStatement();
            if (sql.contains("select") || sql.contains("SELECT")) {                
                resultSet = stat.executeQuery(sql);
            } else {
                //INSERT, UPDATE
                stat.executeUpdate(sql);
                resultSet = null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static ResultSet getSqlResult(String sqlQuery) {
        sqlQueryString(sqlQuery);
        return resultSet;
    }

    public static Connection getDBConnection() {
        connectOnDemand();
        return mysqlConnection;
    }
}
