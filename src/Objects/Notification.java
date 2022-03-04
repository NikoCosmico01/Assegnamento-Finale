package Objects;

import java.io.Serializable;

public class Notification implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String objectString;
	private String Description;
	private Integer remainingDays;
	private Double amountDouble;
	
	public Notification(String objectstring, String description ,Integer remainingdays, Double amountdouble) {
		this.objectString = objectstring;
		this.Description = description;
		this.remainingDays = remainingdays;
		this.amountDouble = amountdouble;
	}
	public String getObjectString() {
		return this.objectString;
	}
	public String getDescription() {
		return this.Description;
	}
	public Integer getDays() {
		return this.remainingDays;
	}
	public Double getAmount() {
		return this.amountDouble;
	}
}
