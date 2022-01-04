package Controller_FXML;

import java.text.ParseException;
import java.util.regex.Pattern;

import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

import People.Person;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class Controller_Pay {

    @FXML private ToggleGroup Casual;
    @FXML private ToggleGroup Type;
    
    @FXML private TextField addressField;
    @FXML private TextField cardNumberField;
    
    @FXML private RadioButton boatFee;
    @FXML private RadioButton cardRadio;
    @FXML private RadioButton compFee;
    @FXML private RadioButton ibanRadio;
    @FXML private RadioButton membFee;
    
    @FXML private TextField cvcField;
    @FXML private TextField expiryField;
    @FXML private TextField ibanField;
    @FXML private TextField nameField;
    @FXML private TextField price;
    @FXML private TextField surnameField;
    
    private ToggleGroup purposeToggleGroup;
    private ToggleGroup paymentToggleGroup;
    
    public void initialize(Person P, String paymentPurpose, Double paymentPrice) {
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
    		price.setDisable(true);
    		price.setText(String.valueOf(paymentPrice));
    	}
    }
    
    public void purposeButtonChanged() {
    	if (this.purposeToggleGroup.getSelectedToggle().equals(this.boatFee)) {
    		
    	}
    	if (this.purposeToggleGroup.getSelectedToggle().equals(this.compFee)) {
    		
    	}
    	if (this.purposeToggleGroup.getSelectedToggle().equals(this.membFee)) {
	
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
    
    public void paymentButtonChanged() {
    	if (this.paymentToggleGroup.getSelectedToggle().equals(this.cardRadio)) {
    		ibanField.setVisible(false);
    		cardNumberField.setVisible(true);
    		addTextLimiter(cardNumberField, 16);  		
    		cvcField.setVisible(true);
    		addTextLimiter(cvcField, 3);
    		expiryField.setVisible(true);
    		addTextLimiter(expiryField, 4);    		
    	}
    	if (this.paymentToggleGroup.getSelectedToggle().equals(this.ibanRadio)) {
    		ibanField.setVisible(true);
    		addTextLimiter(ibanField, 30);
    		cardNumberField.setVisible(false);
    		cvcField.setVisible(false);
    		expiryField.setVisible(false);
    	}
    }
 
}
