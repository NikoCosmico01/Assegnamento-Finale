package Objects;

import java.io.Serializable;

/**
 * This object is used to collapse all the boat informations into it.
 * 
 * @author NicoT
 *
 */

public class Boat implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private final String Name;
	private final Integer ID;
	private final Double Length;
	private final String Owner;
	
	/**
	 * Object Empty Constructor
	 */
	public Boat() {
		this.Name = "";
		this.ID = 0;
		this.Length = 0.0;
		this.Owner = "";
	}
	
	/**
	 * Object Constructor
	 * 
	 * @param name Boat Name
	 * @param id Boat ID
	 * @param length Boat Length
	 */
	public Boat(String name, Integer id, Double length) {
		this.Name = name;
		this.ID = id;
		this.Length = length;
		this.Owner = "";
	}
	
	/**
	 * GET Method 
	 * 
	 * @return Boat Name
	 */
	public String getName() {
		return this.Name;
	}
	/**
	 * GET Method 
	 * 
	 * @return Boat ID
	 */
	public Integer getID() {
		return this.ID;
	}
	/**
	 * GET Method 
	 * 
	 * @return Boat Name
	 */
	public Double getLength() {
		return this.Length;
	}
	/**
	 * GET Method 
	 * 
	 * @return Boat Length
	 */
	public String getOwner() {
		return this.Owner;
	}
}
