package model;

/*
 * Bu class, database'teki USER tablosuna karşılık gelir.
 * Login işlemleri ve kullanıcı iletişim bilgileri bu class üzerinden yönetilir.
 */
public class User {

    // USER tablosundaki kolonlar
    private int userId;
    private String name;
    private String email;
    private String password;
    private String role; // ADMIN veya CUSTOMER
    private String phone; // 🔥 Yeni eklenen telefon kolonu

    // Boş constructor
    public User() {
    }

    // Tüm alanları alan constructor (phone dahil edildi)
    public User(int userId, String name, String email, String password, String role, String phone) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
    }

    // Getter - Setter'lar
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}