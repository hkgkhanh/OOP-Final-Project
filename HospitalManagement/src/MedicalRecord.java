import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MedicalRecord {
	private int id;
	private String cccd;
	private int doctorID;
	private String diagnosis;
	private String treatment;
	private String prescription;
	private String dateOfVisit; // ngày nhập viện
	private int lengthOfHospitalStay; // số ngày nằm viện, 0 thì là ko nằm
	private String followUpDate;
	private String note;
	
	public MedicalRecord(int id, String cccd, int doctorID, String diagnosis, String treatment, String prescription,
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCccd() {
		return cccd;
	}

	public void setCccd(String cccd) {
		this.cccd = cccd;
	}

	public int getDoctorID() {
		return doctorID;
	}

	public void setDoctorID(int doctorID) {
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
	
	
}
