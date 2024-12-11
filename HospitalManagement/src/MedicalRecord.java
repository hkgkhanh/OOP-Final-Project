import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MedicalRecord {
	private String id;
	private String cccd;
	private String doctorID;
	private String diagnosis;
	private String treatment;
	private String prescription;
	private String dateOfVisit; // ngày nhập viện
	private int lengthOfHospitalStay; // số ngày nằm viện, 0 thì là ko nằm
	private String followUpDate;
	private String note;
	
	public MedicalRecord(String id, String cccd, String doctorID, String diagnosis, String treatment, String prescription,
			String dateOfVisit, int lengthOfHospitalStay, String followUpDate, String note) {
//		super();
		this.id = id;
		this.cccd = cccd;
		this.doctorID = doctorID;
		this.diagnosis = diagnosis;
		this.treatment = treatment;
		this.prescription = prescription;
		this.dateOfVisit = dateOfVisit;
		this.lengthOfHospitalStay = lengthOfHospitalStay;
		this.followUpDate = followUpDate;
		this.note = note;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCccd() {
		return cccd;
	}

	public void setCccd(String cccd) {
		this.cccd = cccd;
	}

	public String getDoctorID() {
		return doctorID;
	}

	public void setDoctorID(String doctorID) {
		this.doctorID = doctorID;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	public String getPrescription() {
		return prescription;
	}

	public void setPrescription(String prescription) {
		this.prescription = prescription;
	}

	public String getDateOfVisit() {
		return dateOfVisit;
	}

	public void setDateOfVisit(String dateOfVisit) {
		this.dateOfVisit = dateOfVisit;
	}

	public int getLengthOfHospitalStay() {
		return lengthOfHospitalStay;
	}

	public void setLengthOfHospitalStay(int lengthOfHospitalStay) {
		this.lengthOfHospitalStay = lengthOfHospitalStay;
	}

	public String getFollowUpDate() {
		return followUpDate;
	}

	public void setFollowUpDate(String followUpDate) {
		this.followUpDate = followUpDate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public int calcFee() { // cái này để sau nhé Sơn, mấy hôm nữa mình lên công thức
		return 0;
	}
	
	public boolean createRecord() {
        String sql = "INSERT INTO medicalrecord (recordID, cccd, doctorID, diagnosis, treatment, prescription, dateOfVisit, lengthOfHospitalStay, followUpDate, note) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Thiết lập các giá trị thuộc tính của đối tượng Doctor
            stmt.setString(1, this.getId()); // ID bác sĩ
            stmt.setString(2, this.getCccd()); // Mật khẩu
            stmt.setString(3, this.doctorID); // Tên
            stmt.setString(4, this.diagnosis); // Họ
            stmt.setString(5, this.treatment); // Khoa
            stmt.setString(6, this.prescription); // Số điện thoại
            stmt.setString(7, this.dateOfVisit); // Email
            stmt.setString(8, Integer.toString(this.lengthOfHospitalStay)); // Ngày tham gia
            stmt.setString(9, this.followUpDate); // Ngày tham gia
            stmt.setString(10, this.note); // Ngày tham gia

            // Thực hiện lệnh INSERT và kiểm tra kết quả
            return stmt.executeUpdate() > 0; // Trả về true nếu thêm thành công
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm bác sĩ: " + e.getMessage());
            return false; // Trả về false nếu có lỗi
        }
    }
	
	public boolean updateRecord() {
        String sql = "UPDATE medicalrecord SET cccd = ?, doctorID = ?, diagnosis = ?, treatment = ?, prescription = ?, dateOfVisit = ?, lengthOfHospitalStay = ?, followUpDate = ?, note = ? WHERE recordID = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, this.cccd);
            stmt.setString(2, this.doctorID);
            stmt.setString(3, this.diagnosis);
            stmt.setString(4, this.treatment);
            stmt.setString(5, this.prescription);
            stmt.setString(6, this.dateOfVisit);
            stmt.setInt(7, this.lengthOfHospitalStay);
            stmt.setString(8, this.followUpDate);
            stmt.setString(9, this.note);
            stmt.setString(10, this.id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteRecord(String recordID) {
        String sql = "DELETE FROM medicalrecord WHERE recordID = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, recordID); // Gán giá trị doctorID

            return stmt.executeUpdate() > 0; // Trả về true nếu xóa thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu xảy ra lỗi
        }
    }
}
