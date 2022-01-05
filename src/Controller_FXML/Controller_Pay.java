package Controller_FXML;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.regex.Pattern;

import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

import People.Person;
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
    
    private static String Cod_F;
    
    public void initialize(Person P, String paymentPurpose, Double paymentPrice) {    
    	
    	Cod_F = P.getCF();
    	
    	purposeToggleGroup = new ToggleGroup();
    	this.boatFee.setToggleGroup(purposeToggleGroup);
    	this.compFee.setToggleGroup(purposeToggleGroup);
    	this.membFee.setToggleGroup(purposeToggleGroup);
    	
    	paymentToggleGroup = new ToggleGroup();
    	this.cardRadio.setToggleGroup(paymentToggleGroup);
    	this.ibanRadio.setToggleGroup(paymentToggleGroup);
    	
    	nameField.setText(P.getName());
    	surnameField.setText(P.getSurname());
    	addressField.setText(P.getAddress());
    	
    	if (paymentPurpose.equals("compFee")) {
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
    	if (paymentPurpose.equals("boatFee")) {
    		compFee.setDisable(true);
    		membFee.setDisable(true);
    		boatFee.setDisable(true);
    		boatFee.setSelected(true);
    	}
    	
    	if (paymentPrice != 0.0) {
    		price.setText(String.valueOf(paymentPrice));
    	}
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
    	cardRadio.setVisible(true);
    	ibanRadio.setVisible(true);
    	methodMenu.setText("+ Add Method");
    }
    
    public void paymentButtonChanged() {
    	if (this.paymentToggleGroup.getSelectedToggle().equals(this.cardRadio)) {
    		ibanField.setVisible(false);
    		cardNumberField.setVisible(true);
    		addTextLimiter(cardNumberField, 16);  		
    		cvcField.setVisible(true);
    		addTextLimiter(cvcField, 3);
    		expiryMonthField.setVisible(true);
    		expiryYearField.setVisible(true);
    		addTextLimiter(expiryMonthField, 2);  
    		addTextLimiter(expiryYearField, 2);  
    		cardLabel.setVisible(true);
    		exLabel.setVisible(true);
    		CVCLabel.setVisible(true);
    		ibanLabel.setVisible(false);
    	}
    	if (this.paymentToggleGroup.getSelectedToggle().equals(this.ibanRadio)) {
    		ibanField.setVisible(true);
    		addTextLimiter(ibanField, 30);
    		cardNumberField.setVisible(false);
    		cvcField.setVisible(false);
    		expiryMonthField.setVisible(false);
    		expiryYearField.setVisible(false);
    		cardLabel.setVisible(false);
    		exLabel.setVisible(false);
    		ibanLabel.setVisible(true);
    		CVCLabel.setVisible(false);
    	}
    }
    
    public void resetBorder() {
    	System.out.println("CIAO");
    	cardNumberField.setStyle(null);
    	cvcField.setStyle(null);
    	expiryMonthField.setStyle(null);
    	expiryYearField.setStyle(null);
    	ibanField.setStyle(null);
    }
    
    public void Submit() {
    	String cardNumber = cardNumberField.getText();
        String cardCVC = cvcField.getText();
        String cardMonthExpiry = expiryMonthField.getText();
        String cardYearExpiry = expiryYearField.getText();
        String IBAN = ibanField.getText();

    	if (!methodMenu.getText().equals("Choose Method")) {
    		if(cardNumber.length() < 16) {
				cardNumberField.setStyle("-fx-border-color: #8b0000;");
			} if(cardMonthExpiry.length() < 1) {
				expiryMonthField.setStyle("-fx-border-color: #8b0000;");
			} if(cardYearExpiry.length() < 2) {
				expiryYearField.setStyle("-fx-border-color: #8b0000;");
			} if(cardCVC.length() < 3) {
				cvcField.setStyle("-fx-border-color: #8b0000;");
			} if(IBAN.length() < 30) {
				ibanField.setStyle("-fx-border-color: #8b0000;");
			} if ((!cardNumber.isBlank() && !cardCVC.isBlank() && !cardMonthExpiry.isBlank() && !cardYearExpiry.isBlank()) || !IBAN.isBlank()) {
    			
    		} else {
    			error.setTextFill(Color.DARKRED);
    			error.setText("Fill All Fields");
    			error.setVisible(true);
    			PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
    			visiblePause.setOnFinished(Event -> error.setVisible(false));
    			visiblePause.play();
    		}
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
