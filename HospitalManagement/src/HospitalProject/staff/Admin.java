package HospitalProject.staff;
import javax.swing.*;

import HospitalProject.dbController.DatabaseConnection;
import HospitalProject.patient.MedicalRecord;
import HospitalProject.patient.Patient;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.text.NumberFormat;
import java.util.Locale;

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
        JPanel patientPanel = createManagementPanel_Patient("Bệnh nhân", "patient");
        // Panel for Doctor Management
        JPanel doctorPanel = createManagementPanel_Doctor("Bác sĩ", "doctor");
        // Panel for Room Management (no count)
        JPanel recordPanel = createManagementPanel_Record("Hồ sơ bệnh án", "medicalrecord");

        dashboardFrame.add(patientPanel);
        dashboardFrame.add(doctorPanel);
        dashboardFrame.add(recordPanel);
        
        dashboardFrame.setLocationRelativeTo(null); // Center the window
        dashboardFrame.setVisible(true);
    }
    
    private JPanel createManagementPanel_Patient(String title, String type) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Button panel with Add button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton addPatientButton = new JButton("Thêm bệnh nhân");
        addPatientButton.setPreferredSize(new Dimension(150, 25));
        buttonPanel.add(addPatientButton);

        // Count label
        JLabel countLabel = new JLabel();
        if (type != null) {
            int count = getCountFromDatabase(type);
            countLabel.setText(title + " (" + count + ")");
            panel.add(countLabel, BorderLayout.NORTH);
        }

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Danh sách bệnh nhân
        JPanel patientListPanel = new JPanel();
        patientListPanel.setLayout(new BoxLayout(patientListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollablePatientsListPane = new JScrollPane(patientListPanel);
        scrollablePatientsListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollablePatientsListPane, BorderLayout.CENTER);

        // Hiển thị danh sách bệnh nhân ban đầu
        displayPatients(patientListPanel);

        // Hành động nút thêm bệnh nhân
        addPatientButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                createPatientDialog(patientListPanel, countLabel, type);
            }
        });

        return panel;
    }

    private JPanel createManagementPanel_Doctor(String title, String type) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Button panel with Add button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton addDoctorButton = new JButton("Thêm bác sĩ");
        addDoctorButton.setPreferredSize(new Dimension(150, 25));
        buttonPanel.add(addDoctorButton);

        // Count label
        JLabel countLabel = new JLabel();
        if (type != null) {
            int count = getCountFromDatabase(type);
            countLabel.setText(title + " (" + count + ")");
            panel.add(countLabel, BorderLayout.NORTH);
        }

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Danh sách bác sĩ
        JPanel doctorListPanel = new JPanel();
        doctorListPanel.setLayout(new BoxLayout(doctorListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollableDoctorsListPane = new JScrollPane(doctorListPanel);
        scrollableDoctorsListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollableDoctorsListPane, BorderLayout.CENTER);

        // Hiển thị danh sách bác sĩ ban đầu
        displayDoctors(doctorListPanel);

        // Hành động nút thêm bác sĩ
        addDoctorButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                createDoctorDialog(doctorListPanel, countLabel, type);
            }
        });

        return panel;
    }

    
    private JPanel createManagementPanel_Record(String title, String type) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Button panel with Add button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton addRecordButton = new JButton("Thêm bệnh án");
        addRecordButton.setPreferredSize(new Dimension(150, 25));
        buttonPanel.add(addRecordButton);

        // Count label
        JLabel countLabel = new JLabel();
        if (type != null) {
            int count = getCountFromDatabase(type);
            countLabel.setText(title + " (" + count + ")");
            panel.add(countLabel, BorderLayout.NORTH);
        }

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Danh sách bệnh nhân
        JPanel recordListPanel = new JPanel();
        recordListPanel.setLayout(new BoxLayout(recordListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollablePatientsListPane = new JScrollPane(recordListPanel);
        scrollablePatientsListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollablePatientsListPane, BorderLayout.CENTER);

        // Hiển thị danh sách bệnh nhân ban đầu
        displayRecords(recordListPanel);

        // Hành động nút thêm bệnh nhân
        addRecordButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                createRecordDialog(recordListPanel, countLabel, type);
            }
        });

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
                float insurancePayPercent = Float.parseFloat(rs.getString("insurancePayPercent"));
                patients.add(new Patient(cccd, surname, firstname, gender, dateOfBirth, address, phoneNumber, insurancePayPercent));
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
            JLabel cccdLabel = new JLabel("CCCD/CMTND: " + patient.getCccd());
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
                @Override
                public void mouseClicked(MouseEvent e) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa bệnh nhân này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        if (patient.deletePatient(patient.getCccd())) {
                            JOptionPane.showMessageDialog(null, "Xóa bệnh nhân thành công!");
                            displayPatients(panel); // Làm mới danh sách sau khi xóa
                        } else {
                            JOptionPane.showMessageDialog(null, "Xóa bệnh nhân thất bại!");
                        }
                    }
                }
            });

            // Add components to button panel
            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);

            // Add all components to the patient panel
            patientPanel.add(nameLabel);
            patientPanel.add(cccdLabel);
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
        panel.removeAll(); // Xóa toàn bộ nội dung hiện tại của panel

        List<Doctor> doctors = getDoctorData(); // Lấy danh sách bác sĩ từ cơ sở dữ liệu

        for (Doctor doctor : doctors) {
            JPanel doctorPanel = new JPanel(); // Tạo một panel riêng cho mỗi bác sĩ
            doctorPanel.setLayout(new BoxLayout(doctorPanel, BoxLayout.Y_AXIS)); // Sử dụng BoxLayout theo chiều dọc

            // Thêm các thông tin của bác sĩ vào panel
            JLabel nameLabel = new JLabel("Họ và Tên: " + doctor.getSurname() + " " + doctor.getFirstname());
            JLabel phoneNumberLabel = new JLabel("Số điện thoại: " + doctor.getPhoneNumber());
            JLabel emailLabel = new JLabel("Email: " + doctor.getEmail());
            JLabel facultyLabel = new JLabel("Khoa: " + doctor.getFaculty());
            JLabel joinDateLabel = new JLabel("Ngày tham gia: " + doctor.getJoinDate());

            // Panel chứa nút sửa và xóa
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton editButton = new JButton("Sửa");
            JButton deleteButton = new JButton("Xóa");

            // Gán sự kiện cho nút sửa
            editButton.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                editDoctor(doctor, panel); // Gọi hàm chỉnh sửa bác sĩ
                }
            });

            deleteButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa bác sĩ này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        if (doctor.deleteDoctor(doctor.getId())) {
                            JOptionPane.showMessageDialog(null, "Xóa bác sĩ thành công!");
                            displayDoctors(panel); // Làm mới danh sách sau khi xóa
                        } else {
                            JOptionPane.showMessageDialog(null, "Xóa bác sĩ thất bại!");
                        }
                    }
                }
            });


            // Thêm các nút vào buttonPanel
            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);

            // Thêm các thành phần vào doctorPanel
            doctorPanel.add(nameLabel);
            doctorPanel.add(phoneNumberLabel);
            doctorPanel.add(emailLabel);
            doctorPanel.add(facultyLabel);
            doctorPanel.add(joinDateLabel);
            doctorPanel.add(buttonPanel);

            // Thêm doctorPanel vào panel chính
            panel.add(doctorPanel);
        }

        panel.revalidate(); // Làm mới giao diện
        panel.repaint();    // Vẽ lại giao diện
    }

    
    private List<MedicalRecord> getRecordData() {
        List<MedicalRecord> records = new ArrayList<>();
        String query = "SELECT * FROM medicalrecord";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
            	String id = rs.getString("recordID");
            	String cccd = rs.getString("cccd");
            	String doctorID = rs.getString("doctorID");
            	String diagnosis = rs.getString("diagnosis");
            	String treatment = rs.getString("treatment");
            	String prescription = rs.getString("prescription");
            	String dateOfVisit = rs.getString("dateOfVisit");
            	int lengthOfHospitalStay = rs.getInt("lengthOfHospitalStay");
            	String followUpDate = rs.getString("followUpDate");
            	String note = rs.getString("note");
            	boolean paid = rs.getString("paid").equals("1") ? true : false;
            	float subTotalFee = Float.parseFloat(rs.getString("subTotalFee"));
            	records.add(new MedicalRecord(id, cccd, doctorID, diagnosis, treatment, prescription, dateOfVisit, lengthOfHospitalStay, followUpDate, note, paid, subTotalFee));
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
        	JPanel recordPanel = new JPanel(); // Tạo một panel riêng cho mỗi bác sĩ
        	recordPanel.setLayout(new BoxLayout(recordPanel, BoxLayout.Y_AXIS));
            
            JLabel cccdLabel = new JLabel("CCCD: " + record.getCccd());
            JLabel doctorIdLabel = new JLabel("Mã bác sĩ: " + record.getDoctorID());
            JLabel diagnosisLabel = new JLabel("Chẩn đoán: " + record.getDiagnosis());
            JLabel dateOfVisitLabel = new JLabel("Ngày khám: " + record.getDateOfVisit());
            
            @SuppressWarnings("deprecation")
			Locale vnLocale = new Locale("vi", "VN"); 
            NumberFormat vnCurrencyFormat = NumberFormat.getCurrencyInstance(vnLocale);
            JLabel feeLabel = new JLabel("Viện phí: " + (record.getPaid() == false ? vnCurrencyFormat.format(record.calcFee()) : "Đã thanh toán"));
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton editButton = new JButton("Sửa");
            JButton deleteButton = new JButton("Xóa");

            // Set button action listeners
            editButton.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    editRecord(record, panel); // Define this method to edit patient data
                }
            });

            deleteButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa bệnh án này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        if (record.deleteRecord(record.getId())) {
                            JOptionPane.showMessageDialog(null, "Xóa bác sĩ thành công!");
                            displayRecords(panel); // Làm mới danh sách sau khi xóa
                        } else {
                            JOptionPane.showMessageDialog(null, "Xóa bác sĩ thất bại!");
                        }
                    }
                }
            });
            
            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);

            // Add components to the patient panel
            recordPanel.add(cccdLabel);
            recordPanel.add(doctorIdLabel);
            recordPanel.add(diagnosisLabel);
            recordPanel.add(dateOfVisitLabel);
            recordPanel.add(feeLabel);
            recordPanel.add(buttonPanel);

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
        JLabel insuranceLabel = new JLabel("Phần trăm BHYT chi trả: " + patient.getInsurancePayPercent());

        // Thêm các thành phần vào dialog (theo từng dòng)
        editDialog.add(cccdLabel);  // Hiển thị CCCD ở trên cùng, không cho phép chỉnh sửa
        editDialog.add(insuranceLabel);
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
    
    private void editDoctor(Doctor doctor, JPanel panel) {
        // Tạo JDialog để sửa thông tin bác sĩ
        JDialog editDialog = new JDialog();
        editDialog.setTitle("Chỉnh sửa thông tin bác sĩ");
        editDialog.setSize(400, 400);
        editDialog.setLayout(new BoxLayout(editDialog.getContentPane(), BoxLayout.Y_AXIS));

        // Các trường thông tin của bác sĩ
        JLabel idLabel = new JLabel("ID: " + doctor.getId()); // Hiển thị ID, không cho phép chỉnh sửa
        JTextField firstNameField = new JTextField(doctor.getFirstname());
        JTextField lastNameField = new JTextField(doctor.getSurname());
        JTextField phoneField = new JTextField(doctor.getPhoneNumber());
        JTextField emailField = new JTextField(doctor.getEmail());
        JTextField facultyField = new JTextField(doctor.getFaculty());
        JTextField joinDateField = new JTextField(doctor.getJoinDate());

        // Thêm các thành phần vào dialog
        editDialog.add(idLabel);
        editDialog.add(new JLabel("Họ:"));
        editDialog.add(lastNameField);
        editDialog.add(new JLabel("Tên:"));
        editDialog.add(firstNameField);
        editDialog.add(new JLabel("Số điện thoại:"));
        editDialog.add(phoneField);
        editDialog.add(new JLabel("Email:"));
        editDialog.add(emailField);
        editDialog.add(new JLabel("Khoa:"));
        editDialog.add(facultyField);
        editDialog.add(new JLabel("Ngày tham gia:"));
        editDialog.add(joinDateField);

        // Nút lưu thay đổi
        JButton saveButton = new JButton("Lưu");
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Lưu thông tin bác sĩ đã chỉnh sửa
                doctor.setFirstname(firstNameField.getText());
                doctor.setSurname(lastNameField.getText());
                doctor.setPhoneNumber(phoneField.getText());
                doctor.setEmail(emailField.getText());
                doctor.setFaculty(facultyField.getText());
                doctor.setJoinDate(joinDateField.getText());

                // Cập nhật thông tin vào cơ sở dữ liệu
                if (doctor.updateDoctor()) {
                    JOptionPane.showMessageDialog(editDialog, "Cập nhật thành công!");
                    editDialog.dispose(); // Đóng dialog sau khi lưu
                    displayDoctors(panel); // Cập nhật danh sách bác sĩ
                } else {
                    JOptionPane.showMessageDialog(editDialog, "Cập nhật thất bại!");
                }
            }
        });

        editDialog.add(saveButton); // Thêm nút lưu vào cuối dialog

        editDialog.setLocationRelativeTo(null); // Hiển thị dialog ở giữa màn hình
        editDialog.setVisible(true); // Hiển thị dialog
    }

    
    public void createPatientDialog(JPanel patientListPanel, JLabel countLabel, String type) {
        JDialog createDialog = new JDialog();
        createDialog.setTitle("Thêm bệnh nhân mới");
        createDialog.setSize(400, 350);
        createDialog.setLayout(new BoxLayout(createDialog.getContentPane(), BoxLayout.Y_AXIS));

        JTextField cccdField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField dateOfBirthField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField insuranceField = new JTextField();
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"Male", "Female"});

        createDialog.add(new JLabel("CCCD:"));
        createDialog.add(cccdField);
        createDialog.add(new JLabel("Họ:"));
        createDialog.add(lastNameField);
        createDialog.add(new JLabel("Tên:"));
        createDialog.add(firstNameField);
        createDialog.add(new JLabel("Ngày Sinh:"));
        createDialog.add(dateOfBirthField);
        createDialog.add(new JLabel("Số Điện Thoại:"));
        createDialog.add(phoneField);
        createDialog.add(new JLabel("Địa Chỉ:"));
        createDialog.add(addressField);
        createDialog.add(new JLabel("Giới Tính:"));
        createDialog.add(genderComboBox);
        createDialog.add(new JLabel("Phần trăm BHYT chi trả:"));
        createDialog.add(insuranceField);

        JButton saveButton = new JButton("Lưu");
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Patient patient = new Patient(
                    cccdField.getText(),
                    lastNameField.getText(),
                    firstNameField.getText(),
                    (String) genderComboBox.getSelectedItem(),
                    dateOfBirthField.getText(),
                    addressField.getText(),
                    phoneField.getText(),
                    Float.parseFloat(insuranceField.getText())
                );

                if (patient.createPatient()) {
                    JOptionPane.showMessageDialog(null, "Thêm bệnh nhân thành công!");
                    createDialog.dispose();
                    displayPatients(patientListPanel); // Cập nhật danh sách bệnh nhân
                    
                    // Cập nhật countLabel
                    int updatedCount = getCountFromDatabase(type);
                    countLabel.setText("Bệnh nhân (" + updatedCount + ")");
                } else {
                    JOptionPane.showMessageDialog(null, "Thêm bệnh nhân thất bại!");
                }
            }
        });

        createDialog.add(saveButton);

        createDialog.setLocationRelativeTo(null);
        createDialog.setVisible(true);
    }

    public void createDoctorDialog(JPanel doctorListPanel, JLabel countLabel, String type) {
        JDialog createDialog = new JDialog();
        createDialog.setTitle("Thêm bác sĩ mới");
        createDialog.setSize(400, 350);
        createDialog.setLayout(new BoxLayout(createDialog.getContentPane(), BoxLayout.Y_AXIS));

        JTextField idField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField facultyField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField joinDateField = new JTextField();

        createDialog.add(new JLabel("ID:"));
        createDialog.add(idField);
        createDialog.add(new JLabel("Mật khẩu:"));
        createDialog.add(passwordField);
        createDialog.add(new JLabel("Họ:"));
        createDialog.add(lastNameField);
        createDialog.add(new JLabel("Tên:"));
        createDialog.add(firstNameField);
        createDialog.add(new JLabel("Khoa:"));
        createDialog.add(facultyField);
        createDialog.add(new JLabel("Số Điện Thoại:"));
        createDialog.add(phoneField);
        createDialog.add(new JLabel("Email:"));
        createDialog.add(emailField);
        createDialog.add(new JLabel("Ngày tham gia:"));
        createDialog.add(joinDateField);
        
        JButton saveButton = new JButton("Lưu");
        saveButton.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        	    // Kiểm tra đầu vào
        	    if (idField.getText().isEmpty() || lastNameField.getText().isEmpty() || firstNameField.getText().isEmpty() || 
        	        facultyField.getText().isEmpty() || phoneField.getText().isEmpty() || emailField.getText().isEmpty() || 
        	        passwordField.getText().isEmpty() || joinDateField.getText().isEmpty()) {
        	        JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!");
        	        return;
        	    }

        	    // Tạo đối tượng Doctor
        	    Doctor doctor = new Doctor(
        	        idField.getText(),
        	        passwordField.getText(),
        	        lastNameField.getText(),
        	        firstNameField.getText(),
        	        facultyField.getText(),
        	        phoneField.getText(),
        	        emailField.getText(),
        	        joinDateField.getText() // Join date
        	    );

        	    // Gọi phương thức tạo bác sĩ trong cơ sở dữ liệu
        	    if (doctor.createDoctor()) {
        	        JOptionPane.showMessageDialog(null, "Thêm bác sĩ thành công!");
        	        createDialog.dispose();

        	        // Cập nhật danh sách bác sĩ
        	        displayDoctors(doctorListPanel);

        	        // Cập nhật countLabel
        	        int updatedCount = getCountFromDatabase("doctor");
        	        countLabel.setText("Bác sĩ (" + updatedCount + ")");
        	    } else {
        	        JOptionPane.showMessageDialog(null, "Thêm bác sĩ thất bại!");
        	    }
        	}

        });

        createDialog.add(saveButton);

        createDialog.setLocationRelativeTo(null);
        createDialog.setVisible(true);
    }
    
    public void createRecordDialog(JPanel recordListPanel, JLabel countLabel, String type) {
        JDialog createDialog = new JDialog();
        createDialog.setTitle("Thêm bệnh án mới");
        createDialog.setSize(400, 600);
        createDialog.setLayout(new BoxLayout(createDialog.getContentPane(), BoxLayout.Y_AXIS));

        JTextField idField = new JTextField();
        JTextField cccdField = new JTextField();
        JTextField doctorIdField = new JTextField();
        JTextField diagnosisField = new JTextField();
        JTextField treatmentField = new JTextField();
        JTextField prescriptionField = new JTextField();
        JTextField dateOfVisitField = new JTextField();
        JTextField followUpDateField = new JTextField();
        JTextField noteField = new JTextField();

        createDialog.add(new JLabel("Mã bệnh án:"));
        createDialog.add(idField);
        createDialog.add(new JLabel("CCCD:"));
        createDialog.add(cccdField);
        createDialog.add(new JLabel("Mã bác sĩ phụ trách:"));
        createDialog.add(doctorIdField);
        createDialog.add(new JLabel("Chẩn đoán:"));
        createDialog.add(diagnosisField);
        createDialog.add(new JLabel("Phác đồ điều trị:"));
        createDialog.add(treatmentField);
        createDialog.add(new JLabel("Kê đơn thuốc:"));
        createDialog.add(prescriptionField);
        createDialog.add(new JLabel("Ngày khám bệnh:"));
        createDialog.add(dateOfVisitField);
        createDialog.add(new JLabel("Ngày tái khám:"));
        createDialog.add(followUpDateField);
        createDialog.add(new JLabel("Ghi chú của bác sĩ:"));
        createDialog.add(noteField);
        
        JButton saveButton = new JButton("Lưu");
        saveButton.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        	    // Kiểm tra đầu vào
        	    if (idField.getText().isEmpty() || cccdField.getText().isEmpty() || doctorIdField.getText().isEmpty() || 
        	    	diagnosisField.getText().isEmpty() || treatmentField.getText().isEmpty() || prescriptionField.getText().isEmpty() || 
        	    	dateOfVisitField.getText().isEmpty() || noteField.getText().isEmpty()) {
        	        JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!");
        	        return;
        	    }

        	    // Tạo đối tượng Record
        	    MedicalRecord record = new MedicalRecord(
        	        idField.getText(),
        	        cccdField.getText(),
        	        doctorIdField.getText(),
        	        diagnosisField.getText(),
        	        treatmentField.getText(),
        	        prescriptionField.getText(),
        	        dateOfVisitField.getText(),
        	        0,
        	        followUpDateField.getText(),
        	        noteField.getText()
        	    );

        	    // Gọi phương thức tạo record trong cơ sở dữ liệu
        	    if (record.createRecord()) {
        	        JOptionPane.showMessageDialog(null, "Thêm bệnh án thành công!");
        	        createDialog.dispose();

        	        // Cập nhật danh sách record
        	        displayRecords(recordListPanel);

        	        // Cập nhật countLabel
        	        int updatedCount = getCountFromDatabase("medicalrecord");
        	        countLabel.setText("Bệnh án (" + updatedCount + ")");
        	    } else {
        	        JOptionPane.showMessageDialog(null, "Thêm bệnh án thất bại!");
        	    }
        	}

        });

        createDialog.add(saveButton);

        createDialog.setLocationRelativeTo(null);
        createDialog.setVisible(true);
    }

    private void editRecord(MedicalRecord record, JPanel panel) {
        // Tạo JDialog để sửa thông tin bệnh án
        JDialog editDialog = new JDialog();
        editDialog.setTitle("Chỉnh sửa thông tin bệnh án");
        editDialog.setSize(400, 600);
        editDialog.setLayout(new BoxLayout(editDialog.getContentPane(), BoxLayout.Y_AXIS));

        // Các trường thông tin của bệnh án
        JLabel idLabel = new JLabel("ID: " + record.getId()); // Hiển thị ID, không cho phép chỉnh sửa
        JTextField cccdField = new JTextField(record.getCccd());
        JTextField doctorIdField = new JTextField(record.getDoctorID());
        JTextField treatmentField = new JTextField(record.getTreatment());
        JTextField prescriptionField = new JTextField(record.getPrescription());
        JTextField dateOfVisitField = new JTextField(record.getDateOfVisit());
        JTextField lengthOfHospitalStayField = new JTextField(Integer.toString(record.getLengthOfHospitalStay()));
        JTextField followUpDateField = new JTextField(record.getFollowUpDate());
        JTextField noteField = new JTextField(record.getNote());
        JTextField subTotalFeeField = new JTextField(Float.toString(record.getSubTotalFee()));
        JComboBox<String> paidComboBox = new JComboBox<>(new String[]{"Đã thanh toán", "Chưa thanh toán"});
        paidComboBox.setSelectedItem(record.getPaid() == true ? "Đã thanh toán" : "Chưa thanh toán");

        // Thêm các thành phần vào dialog
        editDialog.add(idLabel);
        editDialog.add(new JLabel("CCCD của bệnh nhân:"));
        editDialog.add(cccdField);
        editDialog.add(new JLabel("Mã bác sĩ phụ trách:"));
        editDialog.add(doctorIdField);
        editDialog.add(new JLabel("Phương pháp điều trị:"));
        editDialog.add(treatmentField);
        editDialog.add(new JLabel("Kê đơn thuốc:"));
        editDialog.add(prescriptionField);
        editDialog.add(new JLabel("Ngày khám bệnh:"));
        editDialog.add(dateOfVisitField);
        editDialog.add(new JLabel("Số ngày nằm viện:"));
        editDialog.add(lengthOfHospitalStayField);
        editDialog.add(new JLabel("Ngày tái khám:"));
        editDialog.add(followUpDateField);
        editDialog.add(new JLabel("Ghi chú của bác sĩ:"));
        editDialog.add(noteField);
        editDialog.add(new JLabel("Chi phí khám chữa bệnh:"));
        editDialog.add(subTotalFeeField);
        editDialog.add(paidComboBox);

        // Nút lưu thay đổi
        JButton saveButton = new JButton("Lưu");
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Lưu thông tin bác sĩ đã chỉnh sửa
                record.setCccd(cccdField.getText());
                record.setDoctorID(doctorIdField.getText());
                record.setTreatment(treatmentField.getText());
                record.setPrescription(prescriptionField.getText());
                record.setDateOfVisit(dateOfVisitField.getText());
                record.setLengthOfHospitalStay(Integer.parseInt(lengthOfHospitalStayField.getText()));
                record.setFollowUpDate(followUpDateField.getText());
                record.setNote(noteField.getText());
                record.setSubTotalFee(Float.parseFloat(subTotalFeeField.getText()));
                record.setPaid(((String) paidComboBox.getSelectedItem()) == "Đã thanh toán" ? true : false);

                // Cập nhật thông tin vào cơ sở dữ liệu
                if (record.updateRecord()) {
                    JOptionPane.showMessageDialog(editDialog, "Cập nhật thành công!");
                    editDialog.dispose(); // Đóng dialog sau khi lưu
                    displayRecords(panel); // Cập nhật danh sách bác sĩ
                } else {
                    JOptionPane.showMessageDialog(editDialog, "Cập nhật thất bại!");
                }
            }
        });

        editDialog.add(saveButton); // Thêm nút lưu vào cuối dialog

        editDialog.setLocationRelativeTo(null); // Hiển thị dialog ở giữa màn hình
        editDialog.setVisible(true); // Hiển thị dialog
    }
}
