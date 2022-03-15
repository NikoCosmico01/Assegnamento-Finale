package Controller_FXML;

import java.io.IOException;

import Objects.Person;
import Socket.Client;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This class has the purpose to show all the option needed to add a new user.
 * 
 * @author NicoT
 *
 */

public class Controller_SignUp {

	@FXML private TextField AddressText;
    @FXML private TextField CodF;
    @FXML private PasswordField ConfirmText;
    @FXML private TextField NameText;
    @FXML private PasswordField PasswordText;
    @FXML private TextField SurnameText;
    @FXML private TextField userNameText;
    @FXML private Label error;
    
    private Stage stage;
	private Scene scene;
	private Parent rootParent;
    
	/**
	 * This initialization class shows all the fields to be filled in order to sign up.
	 * 
	 * @param event GUI Click Event
	 * @throws IOException Handles Input-Output Exceptions
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 */
	
    public void signUp(ActionEvent event)throws IOException, ClassNotFoundException{
    	String Name = NameText.getText();
    	String Surname = SurnameText.getText();
		String Address = AddressText.getText();
		String CF = CodF.getText();
		String userName = userNameText.getText();
		String passWord = PasswordText.getText();
		String ConfPassWord = ConfirmText.getText();
		if(!userName.isBlank() && !passWord.isBlank() && !ConfPassWord.isBlank() && !Name.isBlank() && !Surname.isBlank() && !Address.isBlank() && !CF.isBlank()) {
			
			Client.os.writeBytes("checkUser#" + userName + "\n");
			Client.os.flush();
			
			if (Client.is.readByte() == 0) {
				if(passWord.equals(ConfPassWord)) {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("Pay.fxml"));
					rootParent = loader.load();
					Controller_Pay Pay = loader.getController();
					Person P = null;
					stage = (Stage)((Node)event.getSource()).getScene().getWindow();
					scene = new Scene(rootParent);
					Pay.initialize(P, "membFee", 119.90, Name + "#" + Surname + "#" + Address + "#" + CF + "#" + userName + "#" + passWord, stage, scene, 0);
				} else {
					error.setTextFill(Color.DARKRED);
					error.setText("Password Doesn't Match");
					error.setVisible(true);
					PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
					visiblePause.setOnFinished(Event -> error.setVisible(false));
					visiblePause.play();
				}
			} else {
				error.setTextFill(Color.DARKRED);
				error.setText("UserName Still Exists");
				error.setVisible(true);
				PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
				visiblePause.setOnFinished(Event -> error.setVisible(false));
				visiblePause.play();
			}
		} else {
			error.setTextFill(Color.DARKRED);
			error.setText("Some Fields Are Missing");
			error.setVisible(true);
			PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
			visiblePause.setOnFinished(Event -> error.setVisible(false));
			visiblePause.play();
		}

    }
    
    /**
     * This method is called when the "Back" button is pressed and it will show you the previous page.
     * 
     * @param event GUI Click Event
	 * @throws IOException Handles Input-Output Exceptions
     */
    @FXML
    void Back(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIN.fxml"));
		rootParent = loader.load();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(rootParent);
		stage.setScene(scene);
		stage.show();
	}
	
}
