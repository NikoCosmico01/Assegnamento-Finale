package Controller_FXML;

import java.io.IOException;
import java.sql.SQLException;

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

public class Controller_AddBoat {

	private static String Cod_F;
	
	private Stage stage;
	private Scene scene;
	private Parent rootParent;	
	
    @FXML private Label error;
    @FXML private TextField boatLength;
    @FXML private TextField boatName;

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

    @FXML
    void addBoat(ActionEvent event) throws IOException {
    	String bName = boatName.getText();
    	Double bLength = Double.valueOf(boatLength.getText());
    	Client.os.writeBytes("checkBoat#" + Cod_F + "#" + bName + "\n");
		Client.os.flush();
		String returnString = Client.is.readLine();
		if (returnString.equals("OK")) {
			//Procedo Alla Schermata Pagamento
		} else if (returnString.equals("KO")) {
			error.setTextFill(Color.DARKRED);
			error.setText("Boat Still Exists");
			error.setVisible(true);
			PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
			visiblePause.setOnFinished(Event -> error.setVisible(false));
			visiblePause.play();
		} 
    }
    
    public void initialize(String CF) {
    	Cod_F = CF;
    }

}