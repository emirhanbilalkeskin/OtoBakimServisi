package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Database bağlantısını yöneten sınıf
 * (Düzeltilmiş Versiyon - Otomatik Yeniden Bağlanma Özellikli)
 */
public class DatabaseManager {

    // Veritabanı adresi ve ayarları
    private static final String URL =
            "jdbc:mysql://localhost:3306/oto_bakim_servisi?useSSL=false&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = "1234"; // Şifren

    // Bağlantı nesnesi
    private static Connection connection;

    public static Connection getConnection() {
        try {
            // DÜZELTME BURADA YAPILDI:
            // Bağlantı hiç yoksa (null) VEYA var ama kapanmışsa (isClosed) yeniden bağlan.
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database bağlantısı aktif ✅");
            }
        } catch (SQLException e) {
            System.out.println("Database bağlantı hatası ❌");
            e.printStackTrace();
        }
        
        return connection;
    }
}