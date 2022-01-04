package Controller_FXML;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import People.Person;
import Socket.Client;
import Socket.ClientHandler;
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

public class Controller_LogIN {

	@FXML private TextField userNameField;
	@FXML private PasswordField passWordField;
	@FXML private Label error;

	private Stage stage;
	private Scene scene;
	private Parent rootParent;	

	public void login (ActionEvent event) throws SQLException, IOException, ClassNotFoundException{
		String userName = userNameField.getText();
		String passWord = passWordField.getText();

		if (!userName.isBlank() || !passWord.isBlank()) {
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
				System.out.println("Log Manager");
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

	public void registration (ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
		rootParent = loader.load();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(rootParent);
		stage.setScene(scene);
		stage.show();
	}

}
