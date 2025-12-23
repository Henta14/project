package models;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnectTestMain {
    public static void main(String[] args) throws Exception {

        String url = "jdbc:postgresql://localhost:5433/lab_db";
        String user = "postgres";
        String pass = "postgres";


        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            System.out.println("Подключение OK: " + con.getMetaData().getDatabaseProductName());
        }
    }
}
