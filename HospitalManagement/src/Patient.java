import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class Patient {
    private String cccd;
    private String surname;
    private String firstname;
    private String gender;
    private String dateOfBirth;
    private String address;
    private String phoneNumber;
    private float insurancePayPercent;
    private ArrayList<MedicalRecord> medicalRecords;
    
	public Patient(String cccd, String surname, String firstname, String gender, String dateOfBirth, String address, String phoneNumber, float insurancePayPercent) {
		super();
		this.cccd = cccd;
		this.surname = surname;
		this.firstname = firstname;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.insurancePayPercent = insurancePayPercent;
	}

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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public ArrayList<MedicalRecord> getMedicalRecords() {
		return medicalRecords;
	}

	public void setMedicalRecords(ArrayList<MedicalRecord> medicalRecords) {
		this.medicalRecords = medicalRecords;
	}
	
	public float getInsurancePayPercent() {
		return insurancePayPercent;
	}

	public void setInsurancePayPercent(float insurancePayPercent) {
		this.insurancePayPercent = insurancePayPercent;
	}

	public String getCccd() {
		return cccd;
	}
	
	public boolean updatePatient() {
	    String sql = "UPDATE patient SET firstname = ?, surname = ?, gender = ?, phoneNumber = ?, address = ?, insurancePayPercent = ? WHERE cccd = ?";

	    try (Connection conn = DatabaseConnection.connect();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setString(1, this.firstname);
	        stmt.setString(2, this.surname);
	        stmt.setString(3, this.gender);
	        stmt.setString(4, this.phoneNumber);
	        stmt.setString(5, this.address);
	        stmt.setString(6, Float.toString(this.insurancePayPercent));
	        stmt.setString(7, this.cccd);

	        return stmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean createPatient() {
	    String sql = "INSERT INTO patient (cccd, firstname, surname, gender, dateOfBirth, address, phoneNumber, insurancePayPercent) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	    try (Connection conn = DatabaseConnection.connect();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, this.cccd); // Sử dụng `this` để truy cập thuộc tính của đối tượng
	        stmt.setString(2, this.firstname);
	        stmt.setString(3, this.surname);
	        stmt.setString(4, this.gender);
	        stmt.setString(5, this.dateOfBirth);
	        stmt.setString(6, this.address);
	        stmt.setString(7, this.phoneNumber);
	        stmt.setString(8, Float.toString(this.insurancePayPercent));

	        return stmt.executeUpdate() > 0; // Trả về `true` nếu thêm thành công
	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
	        return false; // Trả về `false` nếu có lỗi
	    }
	}

	public boolean deletePatient(String cccd) {
	    String sql = "DELETE FROM patient WHERE cccd = ?";

	    try (Connection conn = DatabaseConnection.connect();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, cccd); // Gán giá trị CCCD

	        return stmt.executeUpdate() > 0; // Trả về true nếu xóa thành công
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false; // Trả về false nếu xảy ra lỗi
	    }
	}

}
