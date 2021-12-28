package Main;

import java.io.IOException;
import java.sql.SQLException;
import Socket.Client;
import Person.Partner;
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

	@FXML
	private TextField userNameField;
	@FXML
	private PasswordField passWordField;
	@FXML
	private Label error;

	private Stage stage;
	private Scene scene;
	private Parent rootParent;	
	
	public void login (ActionEvent event) throws SQLException, IOException, ClassNotFoundException{
		String userName = userNameField.getText();
		String passWord = passWordField.getText();
		
		
		Client.os.writeBytes("registration#pollo#hermano#viabella29#PLLHER#1#pollone#ciaone\n");
		Client.os.flush();
		
		Client.os.writeBytes("connect#" + userName + "#" + passWord + "\n");
		Client.os.flush();
		
		Partner P = (Partner) Client.is.readObject();
		
		if (P != null) {
			System.out.println("Hello " + P.getName());
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Partner.fxml"));
			rootParent = loader.load();
			Controller_Partner Partner = loader.getController();
			Partner.initialize(P.getCF());
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(rootParent);
			stage.setScene(scene);
			Platform.runLater( () -> rootParent.requestFocus() );
			stage.show();
		} else {
			error.setTextFill(Color.WHITE);
			error.setText("Wrong Username Or Password ");
			error.setVisible(true);
			PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
			visiblePause.setOnFinished(Event -> error.setVisible(false));
			visiblePause.play();
		}
	}
	
}
