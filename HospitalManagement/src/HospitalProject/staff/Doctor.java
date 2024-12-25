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
import java.util.*;
import java.util.List;

public class Doctor extends Staff {
        private String surname;
        private String firstname;
        private String faculty;
        private String phoneNumber;
        private String email;
        private String joinDate;
        private ArrayList<Patient> patients;

        public Doctor() {
            // Default constructor
        }

        public Doctor(String id, String password, String surname, String firstname, String faculty, String phoneNumber, String email, String joinDate) {
            super(id, password); // Gọi constructor của Staff
            this.surname = surname;
            this.firstname = firstname;
            this.faculty = faculty;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.joinDate = joinDate;
            this.patients = new ArrayList<>();
        }

        // Getters và Setters cho các thuộc tính riêng
        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getFaculty() {
            return faculty;
        }

        public void setFaculty(String faculty) {
            this.faculty = faculty;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getJoinDate() {
            return joinDate;
        }

        public void setJoinDate(String joinDate) {
            this.joinDate = joinDate;
        }

        public ArrayList<Patient> getPatients() {
            return patients;
        }

        public void setPatients(ArrayList<Patient> patients) {
            this.patients = patients;
        }
        public boolean createDoctor() {
            String sql = "INSERT INTO doctor (doctorID, password, firstname, surname, faculty, phoneNumber, email, joinDate) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DatabaseConnection.connect();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                // Thiết lập các giá trị thuộc tính của đối tượng Doctor
                stmt.setString(1, this.getId()); // ID bác sĩ
                stmt.setString(2, this.getPassword()); // Mật khẩu
                stmt.setString(3, this.firstname); // Tên
                stmt.setString(4, this.surname); // Họ
                stmt.setString(5, this.faculty); // Khoa
                stmt.setString(6, this.phoneNumber); // Số điện thoại
                stmt.setString(7, this.email); // Email
                stmt.setString(8, this.joinDate); // Ngày tham gia

                // Thực hiện lệnh INSERT và kiểm tra kết quả
                return stmt.executeUpdate() > 0; // Trả về true nếu thêm thành công
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi thêm bác sĩ: " + e.getMessage());
                return false; // Trả về false nếu có lỗi
            }
        }
        public boolean updateDoctor() {
            String sql = "UPDATE doctor SET firstname = ?, surname = ?, phoneNumber = ?, email = ?, faculty = ?, joinDate = ? WHERE doctorID = ?";

            try (Connection conn = DatabaseConnection.connect();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, this.firstname);
                stmt.setString(2, this.surname);
                stmt.setString(3, this.phoneNumber);
                stmt.setString(4, this.email);
                stmt.setString(5, this.faculty);
                stmt.setString(6, this.joinDate);
                stmt.setString(7, this.id);

                return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        public boolean deleteDoctor(String doctorID) {
            String sql = "DELETE FROM doctor WHERE doctorID = ?";

            try (Connection conn = DatabaseConnection.connect();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, doctorID); // Gán giá trị doctorID

                return stmt.executeUpdate() > 0; // Trả về true nếu xóa thành công
            } catch (SQLException e) {
                e.printStackTrace();
                return false; // Trả về false nếu xảy ra lỗi
            }
        }

	
	// Method to display login form
    public void showLoginWindow() {
        JFrame loginFrame = new JFrame("Đăng nhập doctor");
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setSize(300, 200);
        loginFrame.setLayout(new GridLayout(3, 2, 10, 10));
        
        JLabel loginIdLabel = new JLabel("Email:");
        JTextField loginIdField = new JTextField(100);
        
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
                email = loginIdField.getText();
                password = new String(passwordField.getPassword());

                if (authenticate(email, password)) {
//                    JOptionPane.showMessageDialog(loginFrame, "Đăng nhập thành công.");
                    loginFrame.dispose(); // Close login window
                    getThisDoctorData(email);
                    showDoctorDashboard(); // Open new window if login successful
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Nhập email hoặc mật khẩu sai, hãy thử lại.");
                }
            }
        });
        
        loginFrame.setLocationRelativeTo(null); // Center the window
        loginFrame.setVisible(true);
    }

    // Method to authenticate the doctor with the database
    private boolean authenticate(String email, String password) {
        boolean isAuthenticated = false;

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT * FROM doctor WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
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
    
    private void getThisDoctorData(String email) {
    	String query = "SELECT * FROM doctor WHERE email = ?";
    	
    	try (Connection conn = DatabaseConnection.connect();
    			PreparedStatement stmt = conn.prepareStatement(query)) {
    		
    		stmt.setString(1, email);
    		ResultSet rs = stmt.executeQuery();
    		
    		while (rs.next()) {
    			this.email = rs.getString("email");
    			this.surname = rs.getString("surname");
    			this.firstname = rs.getString("firstname");
    			this.faculty = rs.getString("faculty");
    			this.phoneNumber = rs.getString("phoneNumber");
    			this.joinDate = rs.getString("joinDate");
    		}
    		
    	} catch (SQLException e) {
    		e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
    	}
    }
    
 // Method to show a new doctor dashboard window after successful login
    private void showDoctorDashboard() {
        JFrame dashboardFrame = new JFrame("Doctor Dashboard");
        dashboardFrame.setSize(800, 600);
        dashboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Navbar
        JLabel welcomeLabel = new JLabel("Xin chào, Bác sĩ " + this.surname + " " + this.firstname + "!", SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JMenuBar menuBar = new JMenuBar();
        JMenu menuLogout = new JMenu("Đăng xuất");
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.add(welcomeLabel, BorderLayout.WEST);
        menuPanel.add(menuLogout, BorderLayout.EAST);
        menuBar.add(menuPanel);
        dashboardFrame.setJMenuBar(menuBar);
        
        dashboardFrame.setLayout(new GridLayout(1, 2, 10, 10)); // 1 row, 2 columns

        JPanel recordPanel = createManagementPanel_Record("Hồ sơ bệnh án", "medicalrecord");
        JPanel patientPanel = createManagementPanel_Patient("Bệnh nhân", "patient", recordPanel);

        dashboardFrame.add(patientPanel);
        dashboardFrame.add(recordPanel);
        
        dashboardFrame.setLocationRelativeTo(null); // Center the window
        dashboardFrame.setVisible(true);
    }
    
    private JPanel createManagementPanel_Patient(String title, String type, JPanel recordPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Count label
        JLabel countLabel = new JLabel();
        if (type != null) {
            int count = getPatientCountFromDatabase();
            countLabel.setText(title + " (" + count + ")");
            panel.add(countLabel, BorderLayout.NORTH);
        }

        // Danh sách bệnh nhân
        JPanel patientListPanel = new JPanel();
        patientListPanel.setLayout(new BoxLayout(patientListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollablePatientsListPane = new JScrollPane(patientListPanel);
        scrollablePatientsListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollablePatientsListPane, BorderLayout.CENTER);

        // Hiển thị danh sách bệnh nhân ban đầu
        displayPatients(patientListPanel, recordPanel);

        return panel;
    }
    
    // Method to get the count from the database
    private int getPatientCountFromDatabase() {
        int count = 0;
        String query = "SELECT COUNT(DISTINCT p.cccd) FROM patient p JOIN medicalrecord mr ON p.cccd = mr.cccd JOIN doctor d ON mr.doctorID = d.doctorID WHERE d.email = '" + this.email + "'";

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
        String query = "SELECT DISTINCT p.* FROM patient p JOIN medicalrecord mr ON p.cccd = mr.cccd JOIN doctor d ON mr.doctorID = d.doctorID WHERE d.email = '" + this.email + "'";

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

    private void displayPatients(JPanel panel, JPanel recordPanel) {
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

            // Add all components to the patient panel
            patientPanel.add(nameLabel);
            patientPanel.add(genderLabel);
            patientPanel.add(dateOfBirthLabel);
            patientPanel.add(phoneNumberLabel);
            
            patientPanel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                	displayRecords(recordPanel, patient);
                }
            });

            // Add patient panel to main panel
            patientPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.add(patientPanel);
        }

        panel.revalidate();
        panel.repaint();
    }
    
    private JPanel createManagementPanel_Record(String title, String type) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Count label
        JLabel countLabel = new JLabel();
        if (type != null) {
            int count = getRecordCountFromDatabase();
            countLabel.setText(title + " (" + count + ")");
            panel.add(countLabel, BorderLayout.NORTH);
        }

        JPanel recordListPanel = new JPanel();        
        recordListPanel.setLayout(new BoxLayout(recordListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollableRecordsListPane = new JScrollPane(recordListPanel);
        scrollableRecordsListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollableRecordsListPane, BorderLayout.CENTER);

        displayRecords(recordListPanel);

        return panel;
    }
    
    private int getRecordCountFromDatabase() {
        int count = 0;
        String query = "SELECT COUNT(*) FROM medicalrecord mr JOIN doctor d ON mr.doctorID = d.doctorID WHERE d.email = '" + this.email + "'";

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
    
    private void displayRecords(JPanel panel) {
        panel.removeAll(); // Clear any existing content

        List<MedicalRecord> records = getRecordData();
        for (MedicalRecord record : records) {
        	JPanel recordPanel = new JPanel(); // Tạo một panel riêng cho mỗi bác sĩ
        	recordPanel.setLayout(new BoxLayout(recordPanel, BoxLayout.Y_AXIS));
            
            JLabel cccdLabel = new JLabel("CCCD: " + record.getCccd());
            JLabel diagnosisLabel = new JLabel("Chẩn đoán: " + record.getDiagnosis());
            JLabel dateOfVisitLabel = new JLabel("Ngày khám: " + record.getDateOfVisit());
                        
            JButton editButton = new JButton("Cập nhật bệnh án");

            // Set button action listeners
            editButton.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    editRecord(record, panel); // Define this method to edit patient data
                }
            });

            // Add components to the patient panel
            recordPanel.add(cccdLabel);
            recordPanel.add(diagnosisLabel);
            recordPanel.add(dateOfVisitLabel);
            recordPanel.add(editButton);

            // Add patient panel to main panel
            recordPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.add(recordPanel);
        }

        panel.revalidate();
        panel.repaint();
    }
    
    private void displayRecords(JPanel panel, Patient patient) {
    	JPanel recordListPanel = (JPanel) ((JScrollPane) panel.getComponents()[1]).getViewport().getView();
    	recordListPanel.removeAll(); // Clear any existing content

        List<MedicalRecord> records = getRecordData();
        for (MedicalRecord record : records) {
        	if (record.getCccd().equals(patient.getCccd())) {
	        	JPanel recordPanel = new JPanel(); // Tạo một panel riêng cho mỗi bác sĩ
	        	recordPanel.setLayout(new BoxLayout(recordPanel, BoxLayout.Y_AXIS));
	            
	            JLabel cccdLabel = new JLabel("CCCD: " + record.getCccd());
	            JLabel diagnosisLabel = new JLabel("Chẩn đoán: " + record.getDiagnosis());
	            JLabel dateOfVisitLabel = new JLabel("Ngày khám: " + record.getDateOfVisit());
	                        
	            JButton editButton = new JButton("Cập nhật bệnh án");
	
	            // Set button action listeners
	            editButton.addMouseListener(new MouseAdapter() {
	                public void mouseClicked(MouseEvent e) {
	                    editRecord(record, recordListPanel); // Define this method to edit patient data
	                }
	            });
	
	            // Add components to the patient panel
	            recordPanel.add(cccdLabel);
	            recordPanel.add(diagnosisLabel);
	            recordPanel.add(dateOfVisitLabel);
	            recordPanel.add(editButton);
	
	            // Add patient panel to main panel
	            recordPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	            recordListPanel.add(recordPanel);
        	}
        }

        panel.revalidate();
        panel.repaint();
    }
    
    private List<MedicalRecord> getRecordData() {
        List<MedicalRecord> records = new ArrayList<>();
        String query = "SELECT mr.* FROM medicalrecord mr JOIN doctor d ON mr.doctorID = d.doctorID WHERE d.email = '" + this.email + "'";

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
            	boolean paid;
            	if (rs.getString("paid") == null) {
            		paid = false;
            	} else {
            		paid = rs.getString("paid").equals("1") ? true : false;
            	}
            	float subTotalFee = 0;
            	if (rs.getString("subTotalFee") != null) {
            		subTotalFee = Float.parseFloat(rs.getString("subTotalFee"));
            	}
            	records.add(new MedicalRecord(id, cccd, doctorID, diagnosis, treatment, prescription, dateOfVisit, lengthOfHospitalStay, followUpDate, note, paid, subTotalFee));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }

        return records;
    }
    
    private void editRecord(MedicalRecord record, JPanel panel) {
        // Tạo JDialog để sửa thông tin bệnh án
        JDialog editDialog = new JDialog();
        editDialog.setTitle("Chỉnh sửa thông tin bệnh án");
        editDialog.setSize(400, 500);
        editDialog.setLayout(new BoxLayout(editDialog.getContentPane(), BoxLayout.Y_AXIS));

        // Các trường thông tin của bệnh án
        JTextField treatmentField = new JTextField(record.getTreatment());
        JTextField prescriptionField = new JTextField(record.getPrescription());
        JTextField dateOfVisitField = new JTextField(record.getDateOfVisit());
        JTextField lengthOfHospitalStayField = new JTextField(Integer.toString(record.getLengthOfHospitalStay()));
        JTextField followUpDateField = new JTextField(record.getFollowUpDate());
        JTextField noteField = new JTextField(record.getNote());

        // Thêm các thành phần vào dialog
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

        // Nút lưu thay đổi
        JButton saveButton = new JButton("Lưu");
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Lưu thông tin bác sĩ đã chỉnh sửa
                record.setTreatment(treatmentField.getText());
                record.setPrescription(prescriptionField.getText());
                record.setDateOfVisit(dateOfVisitField.getText());
                record.setLengthOfHospitalStay(Integer.parseInt(lengthOfHospitalStayField.getText()));
                record.setFollowUpDate(followUpDateField.getText());
                record.setNote(noteField.getText());

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
