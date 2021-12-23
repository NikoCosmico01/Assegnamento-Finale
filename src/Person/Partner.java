package Person;

public class Partner {

	private final String Name;
	private final String Surname;
	private final String CF;
	private final String Address;
	private final String UserName;
	private final String PassWord;
	private final Integer ID_Club;
	
	public Partner() {
		this.Name = "";
		this.Surname = "";
		this.Address = "";
		this.CF = "";
		this.ID_Club = 0;
		this.UserName = "";
		this.PassWord = "";
	}
	
	public Partner(final String name, final String surname, final String address, final String cf,final Integer id_club, final String username, final String password) {
		this.Name = name;
		this.Surname = surname;
		this.Address = address;
		this.CF = cf;
		this.ID_Club = id_club;
		this.UserName = username;
		this.PassWord = password;
	}

	public String getName() {
		return this.Name;
	}
	
	public String getUserName() {
		return this.UserName;
	}

	public String getPassWord() {
		return this.PassWord;
	}

	public String get_CF() {
		return this.CF;
	}
}