package Vehicle;

public class Boat {
	
	private final String Name;
	private final Integer ID;
	private final Double Length;
	private final String Subscription;
	
	public Boat() {
		this.Name = "";
		this.ID = 0;
		this.Length = 0.0;
		this.Subscription = "";
	}
	
	public Boat(String name, Integer id, Double length, String subscription) {
		this.Name = name;
		this.ID = id;
		this.Length = length;
		this.Subscription = subscription;
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
	public String getSubscription() {
		return this.Subscription;
	}
}
