package Objects;

import java.io.Serializable;

/**
 * This object contains all the information needed to properly show a notification with all the
 * needed details.
 * 
 * @author NicoT
 *
 */

public class Notification implements Serializable {

	private static final long serialVersionUID = 1L;
	private String objectString;
	private String Description;
	private Integer remainingDays;
	private Double amountDouble;
	private Integer ID_Boat;
	private Double Length;
	
	/**
	 * Object Constructor
	 * 
	 * @param objectstring Notification Object
	 * @param description Notification Description
	 * @param remainingdays Payment Remaining Days
	 * @param amountdouble Payment Amount
	 * @param id_boat ID Boat (If Needed)
	 * @param length Boat Length (If Needed)
	 */
	
	public Notification(String objectstring, String description ,Integer remainingdays, Double amountdouble, Integer id_boat, Double length) {
		this.objectString = objectstring;
		this.Description = description;
		this.remainingDays = remainingdays;
		this.amountDouble = amountdouble;
		this.ID_Boat = id_boat;
		this.Length = length;
	}
	
	/**
	 * GET Method 
	 * 
	 * @return Notification Object
	 */
	public String getObjectString() {
		return this.objectString;
	}
	/**
	 * GET Method 
	 * 
	 * @return Notification Description
	 */
	public String getDescription() {
		return this.Description;
	}
	/**
	 * GET Method 
	 * 
	 * @return Payment Remaining Days
	 */
	public Integer getDays() {
		return this.remainingDays;
	}
	/**
	 * GET Method 
	 * 
	 * @return Payment Amount
	 */
	public Double getAmount() {
		return this.amountDouble;
	}
	/**
	 * GET Method 
	 * 
	 * @return ID Boat (If Needed)
	 */
	public Integer getID_Boat() {
		return this.ID_Boat;
	}
	/**
	 * GET Method 
	 * 
	 * @return Boat Length (If Needed)
	 */
	public Double getLength() {
		return this.Length;
	}
}
