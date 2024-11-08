import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctor {
    private int id;
    private String name;
    private String address;
    private String contact_no;
    private String date_joining;
    
	public Doctor(int id, String name, String address, String contact_no, String date_joining) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.contact_no = contact_no;
		this.date_joining = date_joining;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact_no() {
		return contact_no;
	}

	public void setContact_no(String contact_no) {
		this.contact_no = contact_no;
	}

	public String getDate_joining() {
		return date_joining;
	}

	public void setDate_joining(String date_joining) {
		this.date_joining = date_joining;
	}
    
	
    
}
