package Controller_FXML;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import Objects.Message;
import Objects.PaymentMethod;
import Objects.Person;
import Socket.Client;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller_Pay {

	@FXML private ToggleGroup Casual;
	@FXML private ToggleGroup Type;

	@FXML private TextField addressField;
	@FXML private TextField cardNumberField;

	@FXML private Button suButton;

	@FXML private RadioButton boatFee;
	@FXML private RadioButton cardRadio;
	@FXML private RadioButton compFee;
	@FXML private RadioButton ibanRadio;
	@FXML private RadioButton membFee;

	@FXML private TextField cvcField;
	@FXML private TextField expiryMonthField;
	@FXML private TextField expiryYearField;
	@FXML private TextField ibanField;
	@FXML private TextField nameField;
	@FXML private TextField price;
	@FXML private TextField surnameField;

	@FXML private MenuButton methodMenu;

	@FXML private Label error;
	@FXML private Label ibanLabel;
	@FXML private Label CVCLabel;
	@FXML private Label exLabel;
	@FXML private Label cardLabel;

	private ToggleGroup purposeToggleGroup;
	private ToggleGroup paymentToggleGroup;

	private static String nameString;
	private static String surnameString;
	private static String Cod_F;
	private static String addressString;
	private static String userNameString;
	private static String passWordString;
	private static String boatName;
	private static Integer boatID;
	private static Double boatLength;
	private static Integer compID;
	private static Integer checkerInteger;

	public void initialize(Person P, String paymentPurpose, Double paymentPrice, String paymentDescription, Stage stage, Scene scene, Integer checker) throws IOException, ClassNotFoundException {    

		String[] description = paymentDescription.split("#");

		checkerInteger = checker;
		
		purposeToggleGroup = new ToggleGroup();
		this.boatFee.setToggleGroup(purposeToggleGroup); //boatName#boatLenght
		this.compFee.setToggleGroup(purposeToggleGroup); //boatId#compId
		this.membFee.setToggleGroup(purposeToggleGroup);

		paymentToggleGroup = new ToggleGroup();
		this.cardRadio.setToggleGroup(paymentToggleGroup);
		this.ibanRadio.setToggleGroup(paymentToggleGroup);

		if (paymentPurpose.equals("compFee")) {
			compID = Integer.parseInt(description[1]);
			boatID = Integer.parseInt(description[0]);
			compFee.setDisable(true);
			membFee.setDisable(true);
			boatFee.setDisable(true);
			compFee.setSelected(true);
		}
		if (paymentPurpose.equals("membFee")) {
			compFee.setDisable(true);
			membFee.setDisable(true);
			boatFee.setDisable(true);
			membFee.setSelected(true);
		}
		if (paymentPurpose.equals("boatFee") && checkerInteger == 0) {
			boatName = description[0];
			boatLength = Double.parseDouble(description[1]);
			compFee.setDisable(true);
			membFee.setDisable(true);
			boatFee.setDisable(true);
			boatFee.setSelected(true);
		} else if (paymentPurpose.equals("boatFee") && checkerInteger == 1) {
			boatID = Integer.parseInt(description[0]);
			compFee.setDisable(true);
			membFee.setDisable(true);
			boatFee.setDisable(true);
			boatFee.setSelected(true);
		}

		if (!membFee.isSelected()) {
			Cod_F = P.getCF();
			nameField.setText(P.getName());
			surnameField.setText(P.getSurname());
			addressField.setText(P.getAddress());
		} else {
			Cod_F = description[3];
			nameField.setText(description[0]);
			surnameField.setText(description[1]);
			addressField.setText(description[2]);
			nameString = description[0];
			surnameString = description[1];
			addressString = description[2];
			userNameString = description[4];
			passWordString = description[5];
		}
		
		if (paymentPrice != 0.0) {
			price.setText(String.valueOf(paymentPrice));
		}
		
		
		Client.os.writeBytes("retrievePaymentMethods#" + Cod_F + "\n");
		Client.os.flush();
		PaymentMethod PayMet = (PaymentMethod) Client.is.readObject();


		while (PayMet != null) {
			final PaymentMethod tempPay = PayMet;
			if (PayMet.getIban().equals("NULL")) {
				MenuItem M1 = new MenuItem("Card That Ends With " + PayMet.getCardEnds());
				methodMenu.getItems().add(M1);
				M1.setOnAction((event) -> {
					methodMenu.setText("Card That Ends With " + tempPay.getCardEnds());
					cardRadio.setVisible(true);
					ibanRadio.setVisible(true);
					cardRadio.setDisable(true);
					ibanRadio.setDisable(true);
					cardRadio.setSelected(true);
					this.paymentButtonChanged();
					cardNumberField.setText(tempPay.getNcard());
					cardNumberField.setDisable(true);
					String[] exp = tempPay.getExpiration().split("/");
					expiryMonthField.setText(exp[0]);
					expiryMonthField.setDisable(true);
					expiryYearField.setText(exp[1]);
					expiryYearField.setDisable(true);
					cvcField.setText(tempPay.getCV2());
					cvcField.setDisable(true);
				});

			} else {
				MenuItem M1 = new MenuItem("IBAN That Ends With " + PayMet.getIbanEnds());
				methodMenu.getItems().add(M1);
				M1.setOnAction((event) -> {
					methodMenu.setText("IBAN That Ends With " + tempPay.getIbanEnds());
					cardRadio.setVisible(true);
					ibanRadio.setVisible(true);
					cardRadio.setDisable(true);
					ibanRadio.setDisable(true);
					ibanRadio.setSelected(true);
					this.paymentButtonChanged();
					ibanField.setText(tempPay.getIban());
					ibanField.setDisable(true);
				});
			}
			PayMet = (PaymentMethod) Client.is.readObject();
		}
		
		stage.setScene(scene);
		stage.show();

	}

	public static void addTextLimiter(final TextField textField, final int maxLength) {
		textField.textProperty().addListener((ChangeListener<? super String>) new ChangeListener<String>() {
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
				if (textField.getText().length() > maxLength) {
					String s = textField.getText().substring(0, maxLength);
					textField.setText(s);
				}
			}
		});
	}

	public void showNewMethod() {
		cardRadio.setDisable(false);
		cardRadio.setVisible(true);
		ibanRadio.setDisable(false);
		ibanRadio.setVisible(true);
		cardNumberField.setDisable(false);
		expiryMonthField.setDisable(false);
		expiryYearField.setDisable(false);
		cvcField.setDisable(false);
		methodMenu.setText("+ Add Method");
	}

	public void paymentButtonChanged() {
		if (this.paymentToggleGroup.getSelectedToggle().equals(this.cardRadio)) {
			ibanField.setVisible(false);
			cardNumberField.setVisible(true);
			cardNumberField.setDisable(false);
			addTextLimiter(cardNumberField, 16);  		
			cvcField.setVisible(true);
			cvcField.setDisable(false);
			addTextLimiter(cvcField, 3);
			expiryMonthField.setVisible(true);
			expiryMonthField.setDisable(false);
			expiryYearField.setVisible(true);
			expiryYearField.setDisable(false);
			addTextLimiter(expiryMonthField, 2);  
			addTextLimiter(expiryYearField, 2);  
			cardLabel.setVisible(true);
			exLabel.setVisible(true);
			CVCLabel.setVisible(true);
			ibanLabel.setVisible(false);
		}
		if (this.paymentToggleGroup.getSelectedToggle().equals(this.ibanRadio)) {
			ibanField.setVisible(true);
			addTextLimiter(ibanField, 27);
			cardNumberField.setVisible(false);
			cvcField.setVisible(false);
			expiryMonthField.setVisible(false);
			expiryYearField.setVisible(false);
			cardLabel.setVisible(false);
			exLabel.setVisible(false);
			ibanLabel.setVisible(true);
			CVCLabel.setVisible(false);
			ibanField.setDisable(false);
		}
	}

	public void resetBorder() {
		cardNumberField.setStyle(null);
		cvcField.setStyle(null);
		expiryMonthField.setStyle(null);
		expiryYearField.setStyle(null);
		ibanField.setStyle(null);
	}
	
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}

	public void Submit(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		String cardNumber = cardNumberField.getText();
		String cardCVC = cvcField.getText();
		String cardMonthExpiry = expiryMonthField.getText();
		String cardYearExpiry = expiryYearField.getText();
		String IBAN = ibanField.getText();

		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		Date expDate = new Date(System.currentTimeMillis());
		expDate.setYear(expDate.getYear()+1);

		String payNumberString = "";

		if (!methodMenu.getText().equals("Choose Method")) {
			if (methodMenu.getText().equals("+ Add Method")) {
				if(cardRadio.isSelected()) {
					Integer checkInteger = 0;
					if(cardNumber.length() < 16 || !isNumeric(cardNumber)) {
						cardNumberField.setStyle("-fx-border-color: #8b0000;");
						checkInteger = 1;
					} if(cardMonthExpiry.length() < 1 || !isNumeric(cardMonthExpiry)) {
						expiryMonthField.setStyle("-fx-border-color: #8b0000;");
						checkInteger = 1;
					} if(cardYearExpiry.length() < 2 || !isNumeric(cardYearExpiry)) {
						expiryYearField.setStyle("-fx-border-color: #8b0000;");
						checkInteger = 1;
					} if(cardCVC.length() < 3 || !isNumeric(cardCVC)) {
						cvcField.setStyle("-fx-border-color: #8b0000;");
						checkInteger = 1;
					} if (checkInteger == 0) {
						Client.os.writeBytes("addPaymentMethod#" + Cod_F + "#" + cardNumber + "#" + cardMonthExpiry + "/" + cardYearExpiry + "#" + cardCVC + "#" + "NULL" + "\n");
					} else {
						return;
					}
				} else if (ibanRadio.isSelected()) { 
					if(IBAN.length() < 27) {
						ibanField.setStyle("-fx-border-color: #8b0000;"); return;
					} 
					Client.os.writeBytes("addPaymentMethod#" + Cod_F + "#" + "NULL" + "#" + "NULL" + "/" + "NULL" + "#" + "NULL" + "#" + IBAN + "\n");
				}
				Client.os.flush();	
			}
			if (cardRadio.isSelected()) { payNumberString = cardNumber;
			} else { payNumberString = IBAN; }
			String finalNumberString;
			if (payNumberString.length() == 27) {
				finalNumberString = "IBAN That Ends With " + payNumberString.substring(payNumberString.length()-4);
			} else if (payNumberString.length() == 16) {
				finalNumberString = "CARD That Ends With " + payNumberString.substring(payNumberString.length()-4);
			} else {
				throw new IllegalArgumentException("Error!");
			} if (boatFee.isSelected() && checkerInteger == 0) {
				Client.os.writeBytes("addBoat#" + Cod_F + "#" + boatName + "#" + boatLength + "\n");
				Client.os.flush();
				Message M = (Message) Client.is.readObject();
				Client.os.writeBytes("addPayment#" + Cod_F + "#" + M.getMsg() + "#" + formatter.format(date) + "#" + formatter.format(expDate) + "#" + 0 + "#" + "Boat Addon" + "#" + Double.parseDouble(price.getText()) + "#" + finalNumberString + "\n");
				Client.os.flush();
			} else if (boatFee.isSelected() && checkerInteger == 1) {
				Client.os.writeBytes("addPayment#" + Cod_F + "#" + boatID + "#" + formatter.format(date) + "#" + formatter.format(expDate) + "#" + 0 + "#" + "Boat Fee Renewal" + "#" + Double.parseDouble(price.getText()) + "#" + finalNumberString + "\n");
				Client.os.flush();
			} else if (compFee.isSelected()) {
				Client.os.writeBytes(String.format("addEvent#%d#%d\n", compID, boatID));
				Client.os.flush();
				Message M = (Message) Client.is.readObject();
				String[] description = M.getMsg().split("#");
				Client.os.writeBytes("addPayment#" + Cod_F + "#" + description[0] + "#" + formatter.format(date) + "#" + null + "#" + description[1] + "#" + "Competition Addon" + "#" + Double.parseDouble(price.getText()) + "#" + finalNumberString + "\n");
				Client.os.flush();
			} else if (membFee.isSelected() && checkerInteger == 0) {
				Client.os.writeBytes(String.format("registration#%s#%s#%s#%s#%s#%s\n", nameString, surnameString, addressString, Cod_F, userNameString, passWordString));
				Client.os.flush();
				Client.os.writeBytes("addPayment#" + Cod_F + "#" + 0 + "#" + formatter.format(date) + "#" + formatter.format(expDate) + "#" + 0 + "#" + "Membership Registration" + "#" + Double.parseDouble(price.getText()) + "#" + finalNumberString + "\n");
				Client.os.flush();
			} else if (membFee.isSelected() && checkerInteger == 1) {
				Client.os.writeBytes("addPayment#" + Cod_F + "#" + 0 + "#" + formatter.format(date) + "#" + formatter.format(expDate) + "#" + 0 + "#" + "Membership Fee Renewal" + "#" + Double.parseDouble(price.getText()) + "#" + finalNumberString + "\n");
				Client.os.flush();
			}
			//Return To Home
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Partner.fxml"));
			rootParent = loader.load();
			Controller_Partner index = loader.getController();
			index.initialize(Cod_F);
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(rootParent);
			stage.setScene(scene);
			Platform.runLater( () -> rootParent.requestFocus() );
			stage.show();
		} else {
			error.setTextFill(Color.DARKRED);
			error.setText("No Payment Method Selected");
			error.setVisible(true);
			PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
			visiblePause.setOnFinished(Event -> error.setVisible(false));
			visiblePause.play();
		}
	}

	private Stage stage;
	private Scene scene;
	private Parent rootParent;	

	@FXML
	void Back(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		if (compFee.isSelected()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Partner.fxml"));
			rootParent = loader.load();
			Controller_Partner Partner = loader.getController();
			Partner.initialize(Cod_F);
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(rootParent);
			stage.setScene(scene);
			Platform.runLater( () -> rootParent.requestFocus() );
			stage.show();
		} else if (boatFee.isSelected()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("AddBoat.fxml"));
			rootParent = loader.load();
			Controller_AddBoat AddBoat = loader.getController();
			AddBoat.initialize(Cod_F);
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(rootParent);
			stage.setScene(scene);
			Platform.runLater( () -> rootParent.requestFocus() );
			stage.show();
		}
	}

}
