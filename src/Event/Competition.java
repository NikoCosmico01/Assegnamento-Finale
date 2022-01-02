package Event;

import java.io.Serializable;

public class Competition implements Serializable {

	private static final long serialVersionUID = 1L;
	private String Name;
	private  Integer ID;
	private  Double Cost;
	
	public Competition() {
		this.Name = "";
		this.ID = 0;
		this.Cost = 0.0;
	}
	
	public Competition(String name, Integer id, Double cost) {
		this.Name = name;
		this.ID = id;
		this.Cost = cost;
	}
	
	public String getName() {
		return this.Name;
	}
	
	public Integer getID() {
		return this.ID;
	}

	public Double getCost() {
		return this.Cost;
	}
}
