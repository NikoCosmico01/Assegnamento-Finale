package Main;

import java.io.IOException;
import java.sql.SQLException;

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
	
	public void login (ActionEvent event) throws SQLException{
		String userName = userNameField.getText();
		String passWord = passWordField.getText();
		
		Partner P = new Partner();
		P = Link.checkLogin(userName, passWord);
		if (P != null) {
			System.out.println("Hello" + P.getName());
				/*FXMLLoader loader = new FXMLLoader(getClass().getResource("User.fxml"));
				rootParent = loader.load();
				Controller_User User = loader.getController();
				User.initialize(link, U, link.getArrayProduct());
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(rootParent);
				stage.setScene(scene);
				Platform.runLater( () -> rootParent.requestFocus() );
				stage.show();*/
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
