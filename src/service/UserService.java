package service;

import dao.UserDAO;
import model.User;

/*
 * GUI ile DAO arasındaki katman.
 * Kullanıcı işlemlerinin iş mantığı burada yönetilir.
 */
public class UserService {

    private UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    // Login işlemi
    public User login(String email, String password) {
        return userDAO.login(email, password);
    }

    // 🔥 YENİ EKLENEN: Kayıt İşlemi Köprüsü
    // GUI'den gelen User nesnesini DAO'ya gönderir
    public boolean register(User user) {
        return userDAO.register(user);
    }

    // YENİ: Admin panelinde müşteri detaylarını (İsim, Telefon) göstermek için
    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }
}