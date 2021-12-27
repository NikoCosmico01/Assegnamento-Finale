package Person;

public class Competition {

	private String Name;
	private  Integer ID;
	private  Double Cost;
	
	public void Competition() {
		this.Name = "";
		this.ID = 0;
		this.Cost = 0.0;
	}
	
	public void Competition(String name, Integer id, Double cost) {
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
