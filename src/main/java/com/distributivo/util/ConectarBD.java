package com.distributivo.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConectarBD {
    public static Connection connectDatabase(String host, String port, String database, String user, String password) {
        String url = "";
        try {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
            }
            Connection connection = null;
            url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
            connection = DriverManager.getConnection(url,user, password);
            boolean valid = connection.isValid(50000);
            System.out.println(valid ? "TEST OK" : "TEST FAIL");
            return connection;
        } catch (java.sql.SQLException sqle) {
            System.out.println("Error al conectar con la base de datos de PostgreSQL (" + url + "): " + sqle);
            return null;
        }
    }
}
