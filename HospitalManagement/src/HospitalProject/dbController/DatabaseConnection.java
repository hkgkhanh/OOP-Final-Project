package HospitalProject.dbController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hospital"; // Thay đổi tên cơ sở dữ liệu
    private static final String USER = "root"; // Tên người dùng
    private static final String PASSWORD = "123456"; // Mật khẩu

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Kết nối thành công!");
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối: " + e.getMessage());
        }
        return connection;
    }
}
