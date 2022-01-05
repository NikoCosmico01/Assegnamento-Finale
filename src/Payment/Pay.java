package Payment;
import java.io.Serializable;
import People.Person;
public class Pay {
	private static final long serialVersionUID = 1L;
	private String id;
	private Person AccountHolder;
	private String N_CreditCard;
	private String Expiration;
	private String Iban;
	private String CV2;
	private String Description;
	private String date;
	private Double amount;
	
	public Pay() {
		this.id = "";
		this.AccountHolder = new Person();
		this.amount = 0.0;
		this.CV2 = "";
		this.date = "";
		this.Description = "";
		this.Expiration = "";
		this.Iban = "";
		this.N_CreditCard = "";
	}
	public Pay(String id, Person AccountHolder, String description, Double amount, String date, String IBAN) {
		this.id = id;
		this.AccountHolder = AccountHolder;
		this.amount = amount;
		this.CV2 = "";
		this.date = date;
		this.Description = description;
		this.Expiration = "";
		this.Iban = IBAN;
		this.N_CreditCard = "";
	}
	public Pay(String id, Person AccountHolder, String description, Double amount, String date, String N_creditCard, String Expirstion, String CV2) {
		this.id = id;
		this.AccountHolder = AccountHolder;
		this.amount = amount;
		this.CV2 = CV2;
		this.date = date;
		this.Description = description;
		this.Expiration = Expirstion;
		this.Iban = "";
		this.N_CreditCard = N_creditCard;
	}
	public String getId() {
		return this.id;
	}
	public Person getAccountHolder() {
		return this.AccountHolder;
	}
	public Double getamount() {
		return this.amount;
	}
	public String getCV2() {
		return this.CV2;
	}
	public String getDate() {
		return this.date;
	}
	public String getDescriptioin() {
		return this.Description;
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
}
