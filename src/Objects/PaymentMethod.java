package Objects;

import java.io.Serializable;

/**
 * This object is used to contain all the information of a single Payment Method.
 * 
 * @author NicoT
 *
 */

public class PaymentMethod implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String CF;
	private String N_CreditCard;
	private String Expiration;
	private String CV2;
	private String Iban;
	
	/**
	 * Object Empty Constructor
	 */
	
	public PaymentMethod() {
		this.id = 0;
		this.CF = "";
		this.N_CreditCard = "";
		this.Expiration = "";
		this.CV2 = "";
		this.Iban = "";
	}
	
	/**
	 * Object Constructor
	 * 
	 * @param id Payment ID
	 * @param cf Fiscal Code
	 * @param N_creditCard CC Number
	 * @param exp CC Expiration
	 * @param CV2 CC CV2
	 * @param iban IBAN Number
	 */
	
	public PaymentMethod(Integer id, String cf, String N_creditCard, String exp, String CV2, String iban) {
		this.id = id;
		this.CF = cf;
		this.N_CreditCard = N_creditCard;
		this.Expiration = exp;
		this.CV2 = CV2;
		this.Iban = iban;
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
	 * @return Owner Fiscal Code
	 */
	public String getCF() {
		return this.CF;
	}
	/**
	 * GET Method 
	 * 
	 * @return Card CV2
	 */
	public String getCV2() {
		return this.CV2;
	}
	/**
	 * GET Method 
	 * 
	 * @return Card Expiration
	 */
	public String getExpiration() {
		return this.Expiration;
	}
	/**
	 * GET Method 
	 * 
	 * @return IBAN Number
	 */
	public String getIban() {
		return this.Iban;
	}
	/**
	 * GET Method 
	 * 
	 * @return Credit Card Number
	 */
	public String getNcard() {
		return this.N_CreditCard;
	}
	/**
	 * GET Method 
	 * 
	 * @return Last Credit Card 4 Numbers
	 */
	public String getCardEnds() {
		String string = this.N_CreditCard;
		return string.substring(string.length() - 4);
	}
	/**
	 * GET Method 
	 * 
	 * @return Last IBAN 4 Numbers
	 */
	public String getIbanEnds() {
		String string = this.Iban;
		return string.substring(string.length() - 4);
	}
}
