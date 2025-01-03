package HospitalProject;
import javax.swing.*;
import HospitalProject.dbController.DatabaseConnection;
import HospitalProject.staff.Admin;
import HospitalProject.staff.Doctor;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HospitalApp {
	
	public static void showAbout() {
	    String[] columns = {"MSSV", "Họ và tên đệm", "Tên"};
	    Object[][] data = {
	        {20226088, "Nông Quốc", "Khánh"},
	        {20226110, "Nguyễn Trung", "Kiên"},
	        {20226084, "Nguyễn Đăng Phúc", "Hưng"},
	        {20226125, "Phạm Thái", "Sơn"}
	    };
	    JTable table = new JTable(data, columns);
	    JScrollPane scrollPane = new JScrollPane(table);
	    JFrame tableFrame = new JFrame("Thành viên nhóm 12.1");
	    tableFrame.add(scrollPane);
	    tableFrame.setSize(400, 300);
	    tableFrame.setVisible(true);
	}
	
	public static void main(String[] args) {
        JFrame frame = new JFrame("Phần mềm Quản lý Bệnh viện");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        
        // Kết nối đến DB
        DatabaseConnection.connect();

        // Navbar
        JMenuBar menuBar = new JMenuBar();
        JMenu menuAbout = new JMenu("Thông tin thành viên nhóm 12.1");
        menuBar.add(menuAbout);
        frame.setJMenuBar(menuBar);

        menuAbout.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showAbout();
            }
        });

//        // Banner
//        ImageIcon bannerIcon = new ImageIcon("C:/Users/NONG QUOC KHANH/Pictures/Screenshots/deep thought.png"); // Thay đổi đường dẫn
//        JLabel bannerLabel = new JLabel(bannerIcon);
//        frame.add(bannerLabel, BorderLayout.NORTH);

        // Tạo một JPanel cho tiêu đề
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Phần mềm quản lý bệnh viện", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48)); // Đặt font và kích thước
        titlePanel.add(titleLabel);
//        frame.add(titlePanel, BorderLayout.CENTER); // Thêm titlePanel vào giữa frame
        
        // Panel chứa các nút đăng nhập
        JPanel buttonPanel = new JPanel();
        JButton adminLoginButton = new JButton("Đăng nhập admin");
        JButton doctorLoginButton = new JButton("Đăng nhập bác sĩ");
        
        adminLoginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Admin admin = new Admin(); // Create an Admin object
                admin.showLoginWindow(); // Show the Admin login window
            }
        });
        
        doctorLoginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Doctor doctor = new Doctor(); // Create an Doctor object
                doctor.showLoginWindow(); // Show the Doctor login window
            }
        });
        
        // Thêm các nút vào buttonPanel
        buttonPanel.add(adminLoginButton);
        buttonPanel.add(doctorLoginButton);

        // Thêm buttonPanel vào titlePanel
        titlePanel.add(buttonPanel, BorderLayout.CENTER);

        frame.add(titlePanel, BorderLayout.CENTER); // Thêm titlePanel vào giữa frame

        // Nội dung chính
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        frame.add(mainPanel, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }
}
