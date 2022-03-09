package Controller_FXML;

import java.io.IOException;
import java.sql.SQLException;

import Objects.Person;
import Socket.Client;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
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
 * This class is the first that appears when the client is started; it shows all the fields in order to allow the user or manager to log in the system.
 * 
 * @author NicoT
 *
 */

public class Controller_LogIN {

	@FXML private TextField userNameField;
	@FXML private PasswordField passWordField;
	@FXML private Label error;

	private Stage stage;
	private Scene scene;
	private Parent rootParent;	

	/**
	 * This method is the first that is called when the new page appears; it has the purpose to send the inserted credentials to the
	 * database in order to check if the user/manager exists and, if yes, proceed to call the next appropriate page.
	 * It also check if all the fields have been correctly filled and, if not, it shows an error.
	 * 
	 * @param event GUI Click Event
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */
	
	public void login (ActionEvent event) throws SQLException, IOException, ClassNotFoundException{
		String userName = userNameField.getText();
		String passWord = passWordField.getText();
		
		if (!userName.isBlank() && !passWord.isBlank()) {
			Client.os.writeBytes("connect#" + userName + "#" + passWord + "\n");
			Client.os.flush();
			
			Person P = (Person) Client.is.readObject();
			if (P == null) {
				error.setTextFill(Color.WHITE);
				error.setText("Wrong Username Or Password");
				error.setVisible(true);
				PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
				visiblePause.setOnFinished(Event -> error.setVisible(false));
				visiblePause.play();
			} else if (P.getManager() == 0) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Partner.fxml"));
				rootParent = loader.load();
				Controller_Partner Partner = loader.getController();
				Partner.initialize(P.getCF());
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(rootParent);
				stage.setScene(scene);
				Platform.runLater( () -> rootParent.requestFocus() );
				stage.show();
			} else if (P.getManager() == 1){
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Manager.fxml"));
				rootParent = loader.load();
				loader.getController();
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(rootParent);
				stage.setScene(scene);
				Platform.runLater( () -> rootParent.requestFocus() );
				stage.show();
			}
		}else {
			error.setTextFill(Color.WHITE);
			error.setText("Fill All Fields");
			error.setVisible(true);
			PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
			visiblePause.setOnFinished(Event -> error.setVisible(false));
			visiblePause.play();
		}
	}

	/**
	 * This method is called when the registration button is pressed and return the user to the appropriate page in order
	 * to let him sign up into the system.
	 * 
	 * @param event GUI Click Event
	 * @throws IOException Handles Input-Output Exceptions
	 */
	@FXML
	void registration (ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
		rootParent = loader.load();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(rootParent);
		stage.setScene(scene);
		stage.show();
	}

}
