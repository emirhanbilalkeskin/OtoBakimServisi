package dao;

import model.Service;
import util.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {

    // Tüm hizmetleri listele
    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM service";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                services.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    // ID ile tek bir hizmet getir
    public Service getServiceById(int serviceId) {
        String sql = "SELECT * FROM service WHERE service_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, serviceId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Hizmet ekle (ADMIN) - Başarılıysa true döner
    public boolean addService(Service service) {
        String sql = "INSERT INTO service (name, duration, price) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, service.getName());
            ps.setInt(2, service.getDuration());
            ps.setDouble(3, service.getPrice());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hizmet sil (ADMIN) - Başarılıysa true döner
    public boolean deleteService(int serviceId) {
        String sql = "DELETE FROM service WHERE service_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, serviceId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper: ResultSet verisini Service nesnesine dönüştürür
    private Service mapRow(ResultSet rs) throws SQLException {
        return new Service(
            rs.getInt("service_id"),
            rs.getString("name"),
            rs.getInt("duration"),
            rs.getDouble("price")
        );
    }
}