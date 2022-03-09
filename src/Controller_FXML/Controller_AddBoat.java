package Controller_FXML;

import java.io.IOException;
import java.sql.SQLException;

import Objects.Message;
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
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This class contains all the method used inside the AddBoat GUI, when all the boat information are added by the user and the "SUBMIT" button
 * is pressed, it will redirect the user to the Pay GUI in which the payment must be satisfied in order to successfully add the boat to the Database.
 * 
 * @author NicoT
 *
 */

public class Controller_AddBoat {

	private static String Cod_F;
	
	private Stage stage;
	private Scene scene;
	private Parent rootParent;	
	
    @FXML private Label error;
    @FXML private TextField boatLength;
    @FXML private TextField boatName;

    /**
     * This method is called when the "Back" button is pressed and it will show you the previous page.
     * 
     * @param event GUI Click Event
     * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
     */
    @FXML
    void Back(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("Partner.fxml"));
		rootParent = loader.load();
		Controller_Partner Partner = loader.getController();
		Partner.initialize(Cod_F);
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(rootParent);
		stage.setScene(scene);
		Platform.runLater( () -> rootParent.requestFocus() );
		stage.show();
    }

    /**
     * This method is the one that is called when the "SUBMIT" button is pressed, it checks if all the fields have been
     * correctly filled, if the boat name inserted is still inside the database and, then, it submit all the informations to the PAY page to proceed to the payment.
     * 
     * @param event GUI Click Event
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
     */
    @FXML
    void addBoat(ActionEvent event) throws IOException, ClassNotFoundException {
    	String bName = boatName.getText();
    	String bLength = boatLength.getText();
		if (!bName.isBlank() && !bLength.isBlank()) {
			try {
				@SuppressWarnings("unused")
				Double bLengthDouble = Double.valueOf(bLength);
			} catch (NumberFormatException e) {
				error.setTextFill(Color.DARKRED);
				error.setText("Length Field is Improperly Filled");
				error.setVisible(true);
				PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
				visiblePause.setOnFinished(Event -> error.setVisible(false));
				visiblePause.play();
				return;
			}
			
			Client.os.writeBytes("checkBoat#" + Cod_F + "#" + bName + "\n");
			Client.os.flush();
			Message M = (Message) Client.is.readObject();
			if (M.getMsg().equals("OK")) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Pay.fxml"));
				rootParent = loader.load();
				Controller_Pay Pay = loader.getController();
				Client.os.writeBytes("retrievePerson#" + Cod_F + "\n");
				Client.os.flush();
				Person P = (Person) Client.is.readObject();
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(rootParent);
				Pay.initialize(P, "boatFee", Double.parseDouble(bLength)*10, bName + "#" + bLength, stage, scene, 0);
				
				
			} else if (M.getMsg().equals("KO")) {
				error.setTextFill(Color.DARKRED);
				error.setText("Boat Still Exists");
				error.setVisible(true);
				PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
				visiblePause.setOnFinished(Event -> error.setVisible(false));
				visiblePause.play();
			} 
		} else {
			error.setTextFill(Color.DARKRED);
			error.setText("Fill All Fields");
			error.setVisible(true);
			PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
			visiblePause.setOnFinished(Event -> error.setVisible(false));
			visiblePause.play();
		}
    } 
    
    /**
     * This method is the first that is called when the new page appears.
     * 
     * @param CF Fiscal Code Passed
     */
    public void initialize(String CF) {
    	Cod_F = CF;
    }

}
