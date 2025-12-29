package service;

import dao.AppointmentDAO;
import model.Appointment;
import model.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AppointmentService {
    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private ServiceService serviceService = new ServiceService();

    /**
     * 📅 Yeni Randevu Oluşturma
     * Bu metot, dükkanın 3 araçlık kapasitesini ve hizmet süresini kontrol eder.
     */
    public boolean createAppointment(int userId, int carId, int serviceId, LocalDate date, LocalTime time) {
        // 1. Hizmet bilgilerini veritabanından çek (Süre bilgisini almak için)
        Service selectedService = serviceService.getServiceById(serviceId);
        
        // Güvenlik Kontrolü: Hizmet bulunamazsa işlemi durdur
        if (selectedService == null) {
            System.err.println("Hata: " + serviceId + " ID'li hizmet bulunamadı!");
            return false;
        }
        
        // 2. Kapasite Kontrolü (isSlotOccupied metodu PENDING ve APPROVED randevuları sayar)
        // Eğer seçilen saat diliminde dükkan doluysa (3 araç kuralı) false döner.
        if (appointmentDAO.isSlotOccupied(date, time, selectedService.getDuration())) {
            System.out.println("DEBUG: Kapasite dolu olduğu için randevu reddedildi.");
            return false; 
        }

        // 3. Kontrollerden geçtiyse, randevuyu 'PENDING' (Beklemede) olarak oluştur
        Appointment app = new Appointment(0, userId, carId, serviceId, date, time, "PENDING");
        return appointmentDAO.createAppointment(app);
    }

    public List<Appointment> getAppointmentsByUser(int userId) {
        return appointmentDAO.getAppointmentsByUser(userId);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentDAO.getAllAppointments();
    }

    /**
     * 🔄 Randevu Durumunu Güncelle (Onayla/İptal Et)
     * Admin panelinden gelen 'APPROVED' veya 'CANCELLED' komutlarını işler.
     */
    public boolean updateStatus(int appointmentId, String status) {
        return appointmentDAO.updateStatus(appointmentId, status);
    }
}