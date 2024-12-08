import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class Admin extends Staff {
    
    public Admin() {}

    public Admin(String id, String password) {
        super(id, password); // Kế thừa từ Staff
    }

    // Method to display login form
    public void showLoginWindow() {
        JFrame loginFrame = new JFrame("Đăng nhập admin");
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setSize(300, 200);
        loginFrame.setLayout(new GridLayout(3, 2, 10, 10));
        
        JLabel loginIdLabel = new JLabel("Login ID:");
        JTextField loginIdField = new JTextField(15);
        
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        JPasswordField passwordField = new JPasswordField(15);
        
        JButton loginButton = new JButton("Đăng nhập");
        
        loginFrame.add(loginIdLabel);
        loginFrame.add(loginIdField);
        loginFrame.add(passwordLabel);
        loginFrame.add(passwordField);
        loginFrame.add(new JLabel()); // Empty cell
        loginFrame.add(loginButton);
        
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                id = loginIdField.getText();
                password = new String(passwordField.getPassword());

                if (authenticate(id, password)) {
//                    JOptionPane.showMessageDialog(loginFrame, "Đăng nhập thành công.");
                    loginFrame.dispose(); // Close login window
                    showAdminDashboard(); // Open new window if login successful
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Nhập admin ID hoặc mật khẩu sai, hãy thử lại.");
                }
            }
        });
        
        loginFrame.setLocationRelativeTo(null); // Center the window
        loginFrame.setVisible(true);
    }

    // Method to authenticate the admin with the database
    private boolean authenticate(String id, String password) {
        boolean isAuthenticated = false;

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT * FROM admin WHERE adminID = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                isAuthenticated = true; // Login successful if we get a result
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }

        return isAuthenticated;
    }

    // Method to show a new admin dashboard window after successful login
    private void showAdminDashboard() {
        JFrame dashboardFrame = new JFrame("Admin Dashboard");
        dashboardFrame.setSize(800, 600);
        dashboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Navbar
        JMenuBar menuBar = new JMenuBar();
        JMenu menuPatient = new JMenu("Bệnh nhân");
        JMenu menuDoctor = new JMenu("Bác sĩ");
        JMenu menuAbout = new JMenu("Phòng");
        JMenu menuLogout = new JMenu("Đăng xuất");
        menuBar.add(menuPatient);
        menuBar.add(menuDoctor);
        menuBar.add(menuAbout);
        menuBar.add(menuLogout);
        dashboardFrame.setJMenuBar(menuBar);
        
        JLabel welcomeLabel = new JLabel("Xin chào, " + id + "!", SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
//        dashboardFrame.add(welcomeLabel, BorderLayout.NORTH);
        
        dashboardFrame.setLayout(new GridLayout(1, 3, 10, 10)); // 1 row, 3 columns

        // Panel for Patient Management
        JPanel patientPanel = createManagementPanel("Bệnh nhân", "patient");
        // Panel for Doctor Management
        JPanel doctorPanel = createManagementPanel("Bác sĩ", "doctor");
        // Panel for Room Management (no count)
        JPanel recordPanel = createManagementPanel("Hồ sơ bệnh án", "medicalrecord");

        dashboardFrame.add(patientPanel);
        dashboardFrame.add(doctorPanel);
        dashboardFrame.add(recordPanel);
        
        dashboardFrame.setLocationRelativeTo(null); // Center the window
        dashboardFrame.setVisible(true);
    }
    
    private JPanel createManagementPanel(String title, String type) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));

//        // Title
//        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
//        panel.add(titleLabel, BorderLayout.NORTH);

        // Button panel with Add, Edit, Delete buttons
//        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton addButton = new JButton("Thêm bản ghi");
//        JButton editButton = new JButton("Sửa");
//        JButton deleteButton = new JButton("Xóa");
        
        // Set preferred size for the buttons
        Dimension buttonSize = new Dimension(150, 25); // Adjust size as needed
        addButton.setPreferredSize(buttonSize);
//        editButton.setPreferredSize(buttonSize);
//        deleteButton.setPreferredSize(buttonSize);

        buttonPanel.add(addButton);
//        buttonPanel.add(editButton);
//        buttonPanel.add(deleteButton);

        // Count label (only for patient and doctor)
        JLabel countLabel = new JLabel();
        if (type != null) {
            int count = getCountFromDatabase(type);
            countLabel.setText(title + " (" + count + ")");
            panel.add(countLabel, BorderLayout.NORTH);
        }

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Example ActionListeners for buttons (you may want to define actual logic)
        addButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, "Thêm " + title);
            }
        });

//        editButton.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//                JOptionPane.showMessageDialog(null, "Sửa " + title);
//            }
//        });
//
//        deleteButton.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//                JOptionPane.showMessageDialog(null, "Xóa " + title);
//            }
//        });
        
        // Table for displaying patient records
        if (title.equals("Bệnh nhân")) {
//	        String[] columnNames = {"ID", "Họ tên"};
//	        Object[][] data = getPatientData(); // Get patient data from the database
//	
//	        JTable patientTable = new JTable(data, columnNames);
//	        JScrollPane scrollPane = new JScrollPane(patientTable);
//	        patientTable.setFillsViewportHeight(true); // Ensure the table fills the viewport
//	
//	        panel.add(scrollPane, BorderLayout.CENTER); // Add the table to the center of the panel
        	
        	// Inside showAdminDashboard or createManagementPanel
        	JPanel patientListPanel = new JPanel();
        	patientListPanel.setLayout(new BoxLayout(patientListPanel, BoxLayout.Y_AXIS));
        	displayPatients(patientListPanel); // Populate with patient records
        	
        	// Wrap the patientListPanel in a JScrollPane
        	JScrollPane scrollablePatientsListPane = new JScrollPane(patientListPanel);
        	scrollablePatientsListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        	// Add to main dashboard
        	panel.add(scrollablePatientsListPane);

        } else if (title.equals("Bác sĩ")) {
        	// Inside showAdminDashboard or createManagementPanel
        	JPanel doctorListPanel = new JPanel();
        	doctorListPanel.setLayout(new BoxLayout(doctorListPanel, BoxLayout.Y_AXIS));
        	displayDoctors(doctorListPanel); // Populate with patient records
        	
        	// Wrap the patientListPanel in a JScrollPane
        	JScrollPane scrollableDoctorsListPane = new JScrollPane(doctorListPanel);
        	scrollableDoctorsListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        	// Add to main dashboard
        	panel.add(scrollableDoctorsListPane);
        	
        } else if (title.equals("Hồ sơ bệnh án")) {
        	// Inside showAdminDashboard or createManagementPanel
        	JPanel recordListPanel = new JPanel();
        	recordListPanel.setLayout(new BoxLayout(recordListPanel, BoxLayout.Y_AXIS));
//        	displayRecords(recordListPanel); // Populate with patient records
        	
        	// Wrap the patientListPanel in a JScrollPane
        	JScrollPane scrollableRecordsListPane = new JScrollPane(recordListPanel);
        	scrollableRecordsListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        	// Add to main dashboard
        	panel.add(scrollableRecordsListPane);
        }

        return panel;
    }

    // Method to get the count from the database
    private int getCountFromDatabase(String tableName) {
        int count = 0;
        String query = "SELECT COUNT(*) FROM " + tableName;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                count = rs.getInt(1); // Get the count from the first column
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }

        return count;
    }
    
    private List<Patient> getPatientData() {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM patient";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String cccd = rs.getString("cccd");
                String firstname = rs.getString("firstname");
                String surname = rs.getString("surname");
                String gender = rs.getString("gender");
                String dateOfBirth = rs.getString("dateOfBirth");
                String address = rs.getString("address");
                String phoneNumber = rs.getString("phoneNumber");
                patients.add(new Patient(cccd, surname, firstname, gender, dateOfBirth, address, phoneNumber));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }

        return patients;
    }

//    private void displayPatients(JPanel panel) {
//        panel.removeAll(); // Clear any existing content
//
//        List<Patient> patients = getPatientData();
//        for (Patient patient : patients) {
//            JPanel patientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//            
//            JLabel nameLabel = new JLabel(patient.getSurname() + " " + patient.getFirstname());
//            JLabel genderLabel = new JLabel(patient.getGender());
//            JLabel dateOfBirthLabel = new JLabel(patient.getDateOfBirth());
//            JLabel phoneNumberLabel = new JLabel(patient.getPhoneNumber() );
//            JButton editButton = new JButton("Sửa");
//            JButton deleteButton = new JButton("Xóa");
//
//            // Set button action listeners
//            editButton.addMouseListener(new MouseAdapter() {
//                public void mouseClicked(MouseEvent e) {
////                    editPatient(patient); // Define this method to edit patient data
//                }
//            });
//
//            deleteButton.addMouseListener(new MouseAdapter() {
//                public void mouseClicked(MouseEvent e) {
////                    deletePatient(patient.getId()); // Define this method to delete patient
//                    displayPatients(panel); // Refresh display after deletion
//                }
//            });
//
//            // Add components to the patient panel
//            patientPanel.add(nameLabel);
//            patientPanel.add(genderLabel);
//            patientPanel.add(genderLabel);
//            patientPanel.add(phoneNumberLabel);
//            patientPanel.add(editButton);
//            patientPanel.add(deleteButton);
//
//            // Add patient panel to main panel
//            panel.add(patientPanel);
//        }
//
//        panel.revalidate();
//        panel.repaint();
//    }
    private void displayPatients(JPanel panel) {
        panel.removeAll(); // Clear any existing content

        List<Patient> patients = getPatientData();
        for (Patient patient : patients) {
            JPanel patientPanel = new JPanel();
            patientPanel.setLayout(new BoxLayout(patientPanel, BoxLayout.Y_AXIS)); // Set layout to BoxLayout.Y_AXIS
            
            // Add labels for patient information
            JLabel nameLabel = new JLabel("Họ và Tên: " + patient.getSurname() + " " + patient.getFirstname());
            JLabel genderLabel = new JLabel("Giới tính: " + patient.getGender());
            JLabel dateOfBirthLabel = new JLabel("Ngày sinh: " + patient.getDateOfBirth());
            JLabel phoneNumberLabel = new JLabel("Số điện thoại: " + patient.getPhoneNumber());
            
            // Add buttons for edit and delete
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton editButton = new JButton("Sửa");
            JButton deleteButton = new JButton("Xóa");

            // Set button action listeners
            editButton.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                	editPatient(patient, panel);
                }
            });

            deleteButton.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    // Define delete logic here
                    displayPatients(panel); // Refresh display after deletion
                }
            });

            // Add components to button panel
            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);

            // Add all components to the patient panel
            patientPanel.add(nameLabel);
            patientPanel.add(genderLabel);
            patientPanel.add(dateOfBirthLabel);
            patientPanel.add(phoneNumberLabel);
            patientPanel.add(buttonPanel);

            // Add patient panel to main panel
            panel.add(patientPanel);
        }

        panel.revalidate();
        panel.repaint();
    }


    private List<Doctor> getDoctorData() {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT * FROM doctor";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("doctorID");
                String password = rs.getString("password");
                String firstname = rs.getString("firstname");
                String surname = rs.getString("surname");
                String faculty = rs.getString("faculty");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phoneNumber");
                String joindate = rs.getString("joinDate");
                doctors.add(new Doctor(id, password, surname, firstname, faculty, phoneNumber, email, joindate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }

        return doctors;
    }

    private void displayDoctors(JPanel panel) {
        panel.removeAll(); // Clear any existing content

        List<Doctor> doctors = getDoctorData();
        for (Doctor doctor : doctors) {
            JPanel doctorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            
            JLabel nameLabel = new JLabel(doctor.getSurname() + " " + doctor.getFirstname());
            JButton editButton = new JButton("Sửa");
            JButton deleteButton = new JButton("Xóa");

            // Set button action listeners
            editButton.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
//                    editPatient(patient); // Define this method to edit patient data
                }
            });

            deleteButton.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
//                    deletePatient(patient.getId()); // Define this method to delete patient
                    displayDoctors(panel); // Refresh display after deletion
                }
            });

            // Add components to the patient panel
            doctorPanel.add(nameLabel);
            doctorPanel.add(editButton);
            doctorPanel.add(deleteButton);

            // Add patient panel to main panel
            panel.add(doctorPanel);
        }

        panel.revalidate();
        panel.repaint();
    }
    
    private List<MedicalRecord> getRecordData() {
        List<MedicalRecord> records = new ArrayList<>();
        String query = "SELECT * FROM medicalrecord";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
//                int id = rs.getInt("recordID");
//                String cccd = rs.getString("cccd");
//                int doctorID = rs.getInt("doctorID");
//                String dateOfVisit = rs.getString("dateOfVisit");
//                records.add(new MedicalRecord(id, cccd, doctorID, dateOfVisit));
            	int id = rs.getInt("recordID");
            	String cccd = rs.getString("cccd");
            	int doctorID = rs.getInt("doctorID");
            	String diagnosis = rs.getString("diagnosis");
            	String treatment = rs.getString("treatment");
            	String prescription = rs.getString("diagnosis");
            	String dateOfVisit = rs.getString("dateOfVisit");
            	int lengthOfHospitalStay = rs.getInt("lengthOfHospitalStay");
            	String followUpDate = rs.getString("followUpDate");
            	String note = rs.getString("note");
            	records.add(new MedicalRecord(id, cccd, doctorID, diagnosis, treatment, prescription, dateOfVisit, lengthOfHospitalStay, followUpDate, note));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }

        return records;
    }

    private void displayRecords(JPanel panel) {
        panel.removeAll(); // Clear any existing content

        List<MedicalRecord> records = getRecordData();
        for (MedicalRecord record : records) {
            JPanel recordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            
            JLabel nameLabel = new JLabel(record.getCccd());
            JButton editButton = new JButton("Sửa");
            JButton deleteButton = new JButton("Xóa");

            // Set button action listeners
            editButton.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
//                    editPatient(patient); // Define this method to edit patient data
                }
            });

            deleteButton.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
//                    deletePatient(patient.getId()); // Define this method to delete patient
                    displayRecords(panel); // Refresh display after deletion
                }
            });

            // Add components to the patient panel
            recordPanel.add(nameLabel);
            recordPanel.add(editButton);
            recordPanel.add(deleteButton);

            // Add patient panel to main panel
            panel.add(recordPanel);
        }

        panel.revalidate();
        panel.repaint();
    }
    
    private void editPatient(Patient patient, JPanel panel) {
        // Tạo JDialog để sửa thông tin bệnh nhân
        JDialog editDialog = new JDialog();
        editDialog.setTitle("Chỉnh sửa thông tin bệnh nhân");
        editDialog.setSize(400, 350); // Tăng kích thước một chút
        editDialog.setLayout(new BoxLayout(editDialog.getContentPane(), BoxLayout.Y_AXIS)); // Dùng BoxLayout theo chiều dọc

        // Thêm các trường thông tin bệnh nhân
        JLabel cccdLabel = new JLabel("CCCD: " + patient.getCccd()); // Hiển thị CCCD, không cho phép chỉnh sửa
        JTextField firstNameField = new JTextField(patient.getFirstname());
        JTextField lastNameField = new JTextField(patient.getSurname());
        JTextField dateOfBirthField = new JTextField(patient.getDateOfBirth());
        JTextField phoneField = new JTextField(patient.getPhoneNumber());
        JTextField addressField = new JTextField(patient.getAddress());
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        genderComboBox.setSelectedItem(patient.getGender());

        // Thêm các thành phần vào dialog (theo từng dòng)
        editDialog.add(cccdLabel);  // Hiển thị CCCD ở trên cùng, không cho phép chỉnh sửa
        editDialog.add(new JLabel("Họ:"));
        editDialog.add(lastNameField);
        editDialog.add(new JLabel("Tên:"));
        editDialog.add(firstNameField);
        editDialog.add(new JLabel("Ngày Sinh:"));
        editDialog.add(dateOfBirthField);
        editDialog.add(new JLabel("Số Điện Thoại:"));
        editDialog.add(phoneField);
        editDialog.add(new JLabel("Địa Chỉ:"));
        editDialog.add(addressField);
        editDialog.add(new JLabel("Giới Tính:"));
        editDialog.add(genderComboBox);

        // Nút lưu thay đổi
        JButton saveButton = new JButton("Lưu");
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Lưu thông tin bệnh nhân đã chỉnh sửa
                patient.setFirstname(firstNameField.getText());
                patient.setSurname(lastNameField.getText());
                patient.setDateOfBirth(dateOfBirthField.getText());
                patient.setPhoneNumber(phoneField.getText());
                patient.setAddress(addressField.getText());
                patient.setGender((String) genderComboBox.getSelectedItem());

                // Cập nhật thông tin vào cơ sở dữ liệu
                if (patient.updatePatient()) {
                    JOptionPane.showMessageDialog(editDialog, "Cập nhật thành công!");
                    editDialog.dispose(); // Đóng cửa sổ sửa
                    displayPatients(panel); // Cập nhật lại danh sách bệnh nhân
                } else {
                    JOptionPane.showMessageDialog(editDialog, "Cập nhật thất bại!");
                }
            }
        });
        editDialog.add(saveButton); // Thêm nút lưu vào cuối cửa sổ

        editDialog.setLocationRelativeTo(null); // Đặt dialog ở giữa màn hình
        editDialog.setVisible(true); // Hiển thị dialog
    }



}
