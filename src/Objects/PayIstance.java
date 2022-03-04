package Objects;

import java.io.Serializable;

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
	public Integer getId() {
		return this.id;
	}
	public String getCF() {
		return this.CF;
	}
	public String getBoatName() {
		return this.BoatName + "\n" + this.CompName;
	}
	public String getCurrDate() {
		return this.currDate + "\n" + this.expDate;
	}
	public String getDescription() {
		return Description;
	}
	public Double getAmount() {
		return Amount;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
}
