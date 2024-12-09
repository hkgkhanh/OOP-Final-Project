import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private ArrayList<MedicalRecord> medicalRecords;
    
	public Patient(String cccd, String surname, String firstname, String gender, String dateOfBirth, String address, String phoneNumber) {
		super();
		this.cccd = cccd;
		this.surname = surname;
		this.firstname = firstname;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.phoneNumber = phoneNumber;
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

	public String getCccd() {
		return cccd;
	}
	
	public boolean updatePatient() {
	    String sql = "UPDATE patient SET firstname = ?, surname = ?, gender = ?, phoneNumber = ?, address = ? WHERE cccd = ?";

	    try (Connection conn = DatabaseConnection.connect();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setString(1, this.firstname);
	        stmt.setString(2, this.surname);
	        stmt.setString(3, this.gender);
	        stmt.setString(4, this.phoneNumber);
	        stmt.setString(5, this.address);
	        stmt.setString(6, this.cccd);

	        return stmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean createPatient() {
	    String sql = "INSERT INTO patient (cccd, firstname, surname, gender, dateOfBirth, address, phoneNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";

	    try (Connection conn = DatabaseConnection.connect();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, this.cccd); // Sử dụng `this` để truy cập thuộc tính của đối tượng
	        stmt.setString(2, this.firstname);
	        stmt.setString(3, this.surname);
	        stmt.setString(4, this.gender);
	        stmt.setString(5, this.dateOfBirth);
	        stmt.setString(6, this.address);
	        stmt.setString(7, this.phoneNumber);

	        return stmt.executeUpdate() > 0; // Trả về `true` nếu thêm thành công
	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
	        return false; // Trả về `false` nếu có lỗi
	    }
	}


}
