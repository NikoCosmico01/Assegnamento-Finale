package Controller_FXML;

import java.io.IOException;
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
    
    public void signUp(ActionEvent event)throws IOException{
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
					Client.os.writeBytes(String.format("registration#%s#%s#%s#%s#1#%s#%s\n", Name, Surname, Address, CF, userName, passWord));
					Client.os.flush();
					FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIN.fxml"));
					rootParent = loader.load();
					stage = (Stage)((Node)event.getSource()).getScene().getWindow();
					scene = new Scene(rootParent);
					stage.setScene(scene);
					stage.show();
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
    
    public void Back(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIN.fxml"));
		rootParent = loader.load();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(rootParent);
		stage.setScene(scene);
		stage.show();
	}
	
}
