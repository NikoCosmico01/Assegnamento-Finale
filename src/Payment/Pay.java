package Payment;

import java.io.Serializable;

public class Pay implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String CF;
	private String N_CreditCard;
	private String Expiration;
	private String CV2;
	private String Iban;
	
	public Pay() {
		this.id = 0;
		this.CF = "";
		this.N_CreditCard = "";
		this.Expiration = "";
		this.CV2 = "";
		this.Iban = "";
	}
	public Pay(Integer id, String cf, String N_creditCard, String exp, String CV2, String iban) {
		this.id = id;
		this.CF = cf;
		this.N_CreditCard = N_creditCard;
		this.Expiration = exp;
		this.CV2 = CV2;
		this.Iban = iban;
	}
	public Integer getId() {
		return this.id;
	}
	public String getCF() {
		return this.CF;
	}
	public String getCV2() {
		return this.CV2;
	}
	public String getExpiration() {
		return this.Expiration;
	}
	public String getIban() {
		return this.Iban;
	}
	public String getNcard() {
		return this.N_CreditCard;
	}
	public String getCardEnds() {
		String string = this.N_CreditCard;
		return string.substring(string.length() - 4);
	}
	public String getIbanEnds() {
		String string = this.Iban;
		return string.substring(string.length() - 4);
	}
}
