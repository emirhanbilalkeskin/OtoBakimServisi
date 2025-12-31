package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import service.AppointmentService;
import service.CarService;
import service.ServiceService;
import service.UserService;
import model.Appointment;
import model.Car;
import model.Service;
import model.User;
import java.awt.*;
import java.util.List;

public class AdminFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    
    // Servis Katmanları
    private AppointmentService appointmentService = new AppointmentService();
    private CarService carService = new CarService();
    private ServiceService serviceService = new ServiceService();
    private UserService userService = new UserService();
    
    // Tablo Modelleri
    private DefaultTableModel appointmentTableModel;
    private DefaultTableModel serviceTableModel;
    
    // Fontlar
    private Font tableFont = new Font("Segoe UI", Font.PLAIN, 15);
    private Font headerFont = new Font("Segoe UI", Font.BOLD, 15);
    private Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

    public AdminFrame() {
        setTitle("Oto Servis Yönetim Paneli (ADMİN)");
        setSize(1250, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabbedPane.add("📋 Randevu Yönetimi", createAppointmentManagementPanel());
        tabbedPane.add("🛠️ Hizmet Yönetimi", createServiceManagementPanel());

        add(tabbedPane);
    }

    // --- RANDEVU YÖNETİM PANELİ ---
    private JPanel createAppointmentManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] kolonlar = {"ID", "Müşteri", "Telefon", "Araç/Plaka", "Hizmet", "Tarih/Saat", "Durum"};
        appointmentTableModel = new DefaultTableModel(kolonlar, 0);
       
        JTable table = createStyledTable(appointmentTableModel, true);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        JButton btnApprove = createLargeButton("✅ SEÇİLİ RANDEVUYU ONAYLA", new Color(46, 204, 113));
        JButton btnCancel = createLargeButton("❌ SEÇİLİ RANDEVUYU İPTAL ET", new Color(231, 76, 60));

        buttonPanel.add(btnApprove);
        buttonPanel.add(btnCancel);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        btnApprove.addActionListener(e -> updateAppointmentStatus(table, "APPROVED"));
        btnCancel.addActionListener(e -> updateAppointmentStatus(table, "CANCELLED"));

        loadAllAppointments();
        return panel;
    }

    // --- HİZMET YÖNETİM PANELİ (YENİLENMİŞ - NİZAMİ) ---
    private JPanel createServiceManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form Paneli (GridBagLayout ile hizalı)
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                "Yeni Hizmet Ekle", 
                javax.swing.border.TitledBorder.CENTER, 
                javax.swing.border.TitledBorder.TOP, 
                new Font("Segoe UI", Font.BOLD, 14)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margin
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Inputlar
        JTextField txtServiceName = createStyledTextField();
        JTextField txtDuration = createStyledTextField();
        JTextField txtPrice = createStyledTextField();

        // 1. Satır: Hizmet Adı
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel lblName = new JLabel("Hizmet Adı:", SwingConstants.RIGHT);
        lblName.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        inputPanel.add(lblName, gbc);

        gbc.gridx = 1; 
        gbc.weightx = 0.7;
        inputPanel.add(txtServiceName, gbc);

        // 2. Satır: Süre
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel lblDuration = new JLabel("Süre (Dakika):", SwingConstants.RIGHT);
        lblDuration.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        inputPanel.add(lblDuration, gbc);

        gbc.gridx = 1; 
        gbc.weightx = 0.7;
        inputPanel.add(txtDuration, gbc);

        // 3. Satır: Fiyat
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel lblPrice = new JLabel("Fiyat (TL):", SwingConstants.RIGHT);
        lblPrice.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        inputPanel.add(lblPrice, gbc);

        gbc.gridx = 1; 
        gbc.weightx = 0.7;
        inputPanel.add(txtPrice, gbc);

        // 4. Satır: Ekle Butonu
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.LINE_END; // Sağa yasla
        
        JButton btnAddService = createLargeButton("➕ SİSTEME EKLE", new Color(52, 152, 219));
        btnAddService.setPreferredSize(new Dimension(200, 40));
        btnAddService.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(btnAddService, gbc);

        // Formu sarmala (Ortalamak için)
        JPanel topWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        inputPanel.setPreferredSize(new Dimension(600, 260));
        topWrapper.add(inputPanel);
        panel.add(topWrapper, BorderLayout.NORTH);

        // Tablo
        String[] kolonlar = {"ID", "Hizmet Adı", "Süre (Dk)", "Fiyat (TL)"};
        serviceTableModel = new DefaultTableModel(kolonlar, 0);
        JTable table = createStyledTable(serviceTableModel, false);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Sil Butonu
        JButton btnDeleteService = createLargeButton("🗑️ SEÇİLİ HİZMETİ SİL", new Color(231, 76, 60));
        panel.add(btnDeleteService, BorderLayout.SOUTH);

        // Action Listener - Ekleme
        btnAddService.addActionListener(e -> {
            try {
                String name = txtServiceName.getText().trim();
                String durStr = txtDuration.getText().trim();
                String priceStr = txtPrice.getText().trim();

                if (name.isEmpty() || durStr.isEmpty() || priceStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun.");
                    return;
                }

                int duration = Integer.parseInt(durStr);
                double price = Double.parseDouble(priceStr);

                if (serviceService.addService(name, duration, price)) {
                    JOptionPane.showMessageDialog(this, "Hizmet başarıyla eklendi.");
                    loadServices();
                    txtServiceName.setText(""); txtDuration.setText(""); txtPrice.setText("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Süre ve Fiyat alanlarına sadece sayı giriniz.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage());
            }
        });

        // Action Listener - Silme
        btnDeleteService.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Bu hizmeti silmek istediğinize emin misiniz?", "Onay", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int id = (int) serviceTableModel.getValueAt(row, 0);
                    if (serviceService.deleteService(id)) {
                        JOptionPane.showMessageDialog(this, "Hizmet silindi.");
                        loadServices();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen silinecek bir hizmet seçin.");
            }
        });

        loadServices();
        return panel;
    }

    // --- YARDIMCI METOTLAR ---

    private JTable createStyledTable(DefaultTableModel model, boolean isAppointmentTable) {
        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(tableFont);
        JTableHeader header = table.getTableHeader();
        header.setFont(headerFont);

        if (isAppointmentTable) {
            table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    
                    if (value != null) {
                        String status = value.toString();
                        setHorizontalAlignment(JLabel.CENTER);

                        if (status.equalsIgnoreCase("Onaylandı")) {
                            c.setForeground(new Color(39, 174, 96));
                            setFont(new Font("Segoe UI", Font.BOLD, 15));
                        } else if (status.equalsIgnoreCase("Beklemede")) {
                            c.setForeground(new Color(243, 156, 18));
                            setFont(new Font("Segoe UI", Font.BOLD, 15));
                        } else if (status.equalsIgnoreCase("İptal Edildi")) {
                            c.setForeground(new Color(231, 76, 60));
                            setFont(new Font("Segoe UI", Font.BOLD, 15));
                        }
                    }

                    if (isSelected) {
                        c.setForeground(Color.WHITE);
                    }
                    return c;
                }
            });
        }
        return table;
    }

    // Yeni stil verilmiş TextField oluşturucu
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(200, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            BorderFactory.createEmptyBorder(5, 10, 5, 10) // İç boşluk
        ));
        return field;
    }

    private JButton createLargeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(buttonFont);
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setPreferredSize(new Dimension(0, 50));
        btn.setFocusPainted(false);
        return btn;
    }

    private void loadAllAppointments() {
        appointmentTableModel.setRowCount(0);
        List<Appointment> apps = appointmentService.getAllAppointments();
        for (Appointment a : apps) {
            User u = userService.getUserById(a.getUserId());
            Car c = carService.getCarById(a.getCarId());
            Service s = serviceService.getServiceById(a.getServiceId());
            String durum = "Beklemede";
            if("APPROVED".equals(a.getStatus())) durum = "Onaylandı";
            else if("CANCELLED".equals(a.getStatus())) durum = "İptal Edildi";

            appointmentTableModel.addRow(new Object[]{
                a.getAppointmentId(), (u != null ? u.getName() : "-"), (u != null ? u.getPhone() : "-"),
                (c != null ? c.getPlate() : "-"), (s != null ? s.getName() : "-"),
                a.getDate() + " " + a.getTime(), durum
            });
        }
    }

    private void loadServices() {
        serviceTableModel.setRowCount(0);
        serviceService.getAllServices().forEach(s -> {
            serviceTableModel.addRow(new Object[]{s.getServiceId(), s.getName(), s.getDuration(), s.getPrice()});
        });
    }

    private void updateAppointmentStatus(JTable table, String status) {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = (int) appointmentTableModel.getValueAt(row, 0);
            if (appointmentService.updateStatus(id, status)) {
                loadAllAppointments();
            }
        }
    }
}