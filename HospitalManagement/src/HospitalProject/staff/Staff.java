package HospitalProject.staff;
public class Staff implements Authentication {
    protected String id;       // ID của admin hoặc doctor
    protected String password; // Mật khẩu

    public Staff() {}

    public Staff(String id, String password) {
        this.id = id;
        this.password = password;
    }

    // Getter và Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	@Override
	public boolean authenticate(String credential, String password) {
		// TODO Auto-generated method stub
		return false;
	}

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}
