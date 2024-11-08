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

public class Admin {
	private String login_id;
    private String password;
    
    public Admin() {
        // Default constructor
    }

    public Admin(String login_id, String password) {
        this.login_id = login_id;
        this.password = password;
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
                login_id = loginIdField.getText();
                password = new String(passwordField.getPassword());

                if (authenticate(login_id, password)) {
//                    JOptionPane.showMessageDialog(loginFrame, "Đăng nhập thành công.");
                    loginFrame.dispose(); // Close login window
                    showAdminDashboard(); // Open new window if login successful
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Nhập login id hoặc mật khẩu sai, hãy thử lại.");
                }
            }
        });
        
        loginFrame.setLocationRelativeTo(null); // Center the window
        loginFrame.setVisible(true);
    }

    // Method to authenticate the admin with the database
    private boolean authenticate(String login_id, String password) {
        boolean isAuthenticated = false;

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT * FROM admin WHERE login_id = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, login_id);
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
        
        JLabel welcomeLabel = new JLabel("Xin chào, " + login_id + "!", SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
//        dashboardFrame.add(welcomeLabel, BorderLayout.NORTH);
        
        dashboardFrame.setLayout(new GridLayout(1, 3, 10, 10)); // 1 row, 3 columns

        // Panel for Patient Management
        JPanel patientPanel = createManagementPanel("Bệnh nhân", "patients");
        // Panel for Doctor Management
        JPanel doctorPanel = createManagementPanel("Bác sĩ", "doctors");
        // Panel for Room Management (no count)
        JPanel roomPanel = createManagementPanel("Phòng", "room");

        dashboardFrame.add(patientPanel);
        dashboardFrame.add(doctorPanel);
        dashboardFrame.add(roomPanel);
        
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
        String query = "SELECT * FROM patients";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("patient_id");
                String name = rs.getString("patient_name");
                String fathers_name = rs.getString("fathers_name");
                String address = rs.getString("address");
                String contact_no = rs.getString("contact_no");
                int age = rs.getInt("age");
                patients.add(new Patient(id, name, fathers_name, address, contact_no, age));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }

        return patients;
    }

    private void displayPatients(JPanel panel) {
        panel.removeAll(); // Clear any existing content

        List<Patient> patients = getPatientData();
        for (Patient patient : patients) {
            JPanel patientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            
            JLabel nameLabel = new JLabel(patient.getName());
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
                    displayPatients(panel); // Refresh display after deletion
                }
            });

            // Add components to the patient panel
            patientPanel.add(nameLabel);
            patientPanel.add(editButton);
            patientPanel.add(deleteButton);

            // Add patient panel to main panel
            panel.add(patientPanel);
        }

        panel.revalidate();
        panel.repaint();
    }


    private List<Doctor> getDoctorData() {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT * FROM doctors";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("doctor_id");
                String name = rs.getString("doctor_name");
                String address = rs.getString("address");
                String contact_no = rs.getString("contact_no");
                String date_joining = rs.getString("date_joining");
                doctors.add(new Doctor(id, name, address, contact_no, date_joining));
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
            
            JLabel nameLabel = new JLabel(doctor.getName());
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
}
