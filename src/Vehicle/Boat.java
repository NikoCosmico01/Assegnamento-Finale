package Vehicle;

import java.io.Serializable;

public class Boat implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private final String Name;
	private final Integer ID;
	private final Double Length;
	
	public Boat() {
		this.Name = "";
		this.ID = 0;
		this.Length = 0.0;
	}
	
	public Boat(String name, Integer id, Double length) {
		this.Name = name;
		this.ID = id;
		this.Length = length;
	}
	
	public String getName() {
		return this.Name;
	}
	public Integer getID() {
		return this.ID;
	}
	public Double getLength() {
		return this.Length;
	}
}
