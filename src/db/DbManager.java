package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DbManager {

    private static DbManager instance;

    private final String url;
    private final String user;
    private final String password;

    private DbManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    // Инициализация один раз (удобно в main)
    public static synchronized void init(String url, String user, String password) {
        if (instance != null) {
            throw new IllegalStateException("DbManager уже инициализирован");
        }
        instance = new DbManager(url, user, password);
    }

    public static DbManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("DbManager не инициализирован. Сначала вызови DbManager.init(...)");
        }
        return instance;
    }

    // Делегируем получение соединения сюда
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
    public static synchronized void initIfNeeded(String url, String user, String password) {
        if (instance == null) {
            instance = new DbManager(url, user, password);
        }
    }

}

