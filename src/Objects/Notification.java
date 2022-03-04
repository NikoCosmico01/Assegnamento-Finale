package Objects;

public class Notification {
	private String objectString;
	private String Description;
	private Long remainingDays;
	private Double amountDouble;
	
	public Notification() {
		this.objectString = "";
		this.Description = "";
		this.remainingDays = (long) 0;
		this.amountDouble = 0.0;
	}
	
	public Notification(String objectstring, String description ,Long remainingdays, Double amountdouble) {
		this.objectString = objectstring;
		this.Description = description;
		this.remainingDays = remainingdays;
		this.amountDouble = amountdouble;
	}
	public String getObject() {
		return this.objectString;
	}
	public String getDescription() {
		return this.Description;
	}
	public Long getDays() {
		return this.remainingDays;
	}
	public Double getAmount() {
		return this.amountDouble;
	}
}
