package Objects;

import java.io.Serializable;

public class Person implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String Name;
	private final String Surname;
	private final String CF;
	private final String Address;
	private final String UserName;
	private final String PassWord;
	private final Integer Manager;
	
	public Person() {
		this.Name = "";
		this.Surname = "";
		this.Address = "";
		this.CF = "";
		this.UserName = "";
		this.PassWord = "";
		this.Manager = 0;
	}
	
	public Person(final String name, final String surname, final String address, final String cf, final String username, final String password, final Integer manager) {
		this.Name = name;
		this.Surname = surname;
		this.Address = address;
		this.CF = cf;
		this.UserName = username;
		this.PassWord = password;
		this.Manager = manager;
	}

	public String getName() {
		return this.Name;
	}
	
	public String getSurname() {
		return this.Surname;
	}
	
	public String getAddress() {
		return this.Address;
	}
	
	public String getUserName() {
		return this.UserName;
	}

	public String getPassWord() {
		return this.PassWord;
	}

	public String getCF() {
		return this.CF;
	}
	
	public Integer getManager() {
		return this.Manager;
	}
}