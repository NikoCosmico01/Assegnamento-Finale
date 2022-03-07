package Objects;

import java.io.Serializable;

public class Notification implements Serializable {

	private static final long serialVersionUID = 1L;
	private String objectString;
	private String Description;
	private Integer remainingDays;
	private Double amountDouble;
	private Integer ID_Boat;
	private Double Length;
	
	public Notification(String objectstring, String description ,Integer remainingdays, Double amountdouble, Integer id_boat, Double length) {
		this.objectString = objectstring;
		this.Description = description;
		this.remainingDays = remainingdays;
		this.amountDouble = amountdouble;
		this.ID_Boat = id_boat;
		this.Length = length;
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
	public Integer getID_Boat() {
		return this.ID_Boat;
	}
	public Double getLength() {
		return this.Length;
	}
}
