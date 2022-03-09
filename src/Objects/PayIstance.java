package Objects;

import java.io.Serializable;

/**
 * This object is needed to represent a single payment done with all the details. 
 * (Used in Payment History)
 * 
 * @author NicoT
 *
 */

public class PayIstance implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String CF;
	private String BoatName;
	private String currDate;
	private String expDate;
	private String CompName;
	private String Description;
	private Double Amount;
	private String paymentMethod;

	/**
	 * Object Empty Constructor
	 */
	
	public PayIstance() {
		this.id = 0;
		this.CF = "";
		this.BoatName = "";
		this.currDate = "";
		this.expDate = "";
		this.CompName = "";
		this.Description = "";
		this.Amount = 0.0;
		this.paymentMethod = "";
	}
	
	/**
	 * Object Constructor
	 * 
	 * @param id Payment ID
	 * @param cf Executer Fiscal Code
	 * @param boatname Boat Name
	 * @param currdate Current Date
	 * @param expdate Expiration Date
	 * @param compname Competition Name
	 * @param description Payment Description
	 * @param amount Payment Amount
	 * @param paymentmethod Method Used
	 */
	
	public PayIstance(Integer id, String cf, String boatname, String currdate, String expdate, String compname, String description, Double amount, String paymentmethod) {
		this.id = id;
		this.CF = cf;
		this.BoatName = boatname;
		this.currDate = currdate;
		this.expDate = expdate;
		this.CompName = compname;
		this.Description = description;
		this.Amount = amount;
		this.paymentMethod = paymentmethod;
	}
	
	/**
	 * GET Method 
	 * 
	 * @return Payment ID
	 */
	public Integer getId() {
		return this.id;
	}
	/**
	 * GET Method 
	 * 
	 * @return Executer Fiscal Code
	 */
	public String getCF() {
		return this.CF;
	}
	/**
	 * GET Method 
	 * 
	 * @return Boat Name AND Competition Name
	 */
	public String getBoatName() {
		return this.BoatName + "\n" + this.CompName;
	}
	/**
	 * GET Method 
	 * 
	 * @return Current Date AND Expiration Date
	 */
	public String getCurrDate() {
		return this.currDate + "\n" + this.expDate;
	}
	/**
	 * GET Method 
	 * 
	 * @return Payment Description
	 */
	public String getDescription() {
		return Description;
	}
	/**
	 * GET Method 
	 * 
	 * @return Amount Of The Payment
	 */
	public Double getAmount() {
		return Amount;
	}
	/**
	 * GET Method 
	 * 
	 * @return Last Iban/Card Numbers
	 */
	public String getPaymentMethod() {
		return paymentMethod;
	}
}
