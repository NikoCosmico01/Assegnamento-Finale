package Objects;

import java.io.Serializable;

/**
 * This object contains all the personal informations of a single user or manager.
 * 
 * @author NicoT
 *
 */

public class Person implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String Name;
	private final String Surname;
	private final String CF;
	private final String Address;
	private final String UserName;
	private final String PassWord;
	private final Integer Manager;
	
	/**
	 * Object Empty Constructor
	 */
	
	public Person() {
		this.Name = "";
		this.Surname = "";
		this.Address = "";
		this.CF = "";
		this.UserName = "";
		this.PassWord = "";
		this.Manager = 0;
	}
	
	/**
	 * Object Constructor
	 * 
	 * @param name Person Name
	 * @param surname Person Surname
	 * @param address Person Address
	 * @param cf Person Fiscal Code
	 * @param username Person UserName
	 * @param password Person PassWord
	 * @param manager Manager BIT
	 */
	
	public Person(final String name, final String surname, final String address, final String cf, final String username, final String password, final Integer manager) {
		this.Name = name;
		this.Surname = surname;
		this.Address = address;
		this.CF = cf;
		this.UserName = username;
		this.PassWord = password;
		this.Manager = manager;
	}

	/**
	 * GET Method 
	 * 
	 * @return User Name
	 */
	public String getName() {
		return this.Name;
	}
	/**
	 * GET Method 
	 * 
	 * @return User Surname
	 */
	public String getSurname() {
		return this.Surname;
	}
	/**
	 * GET Method 
	 * 
	 * @return User Address
	 */
	public String getAddress() {
		return this.Address;
	}
	/**
	 * GET Method 
	 * 
	 * @return UserName
	 */
	public String getUserName() {
		return this.UserName;
	}
	/**
	 * GET Method 
	 * 
	 * @return PassWord
	 */
	public String getPassWord() {
		return this.PassWord;
	}
	/**
	 * GET Method 
	 * 
	 * @return User Fiscal Code
	 */
	public String getCF() {
		return this.CF;
	}
	/**
	 * GET Method 
	 * 
	 * @return Manager Bit (1 YES, 0 NO)
	 */
	public Integer getManager() {
		return this.Manager;
	}
}