package Controller_FXML;

import java.io.IOException;
import java.sql.SQLException;

import Event.Participants;
import Socket.Client;
import Vehicle.Boat;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller_Partner {
	
	@FXML private TableColumn<Boat, Double> boatLength;
    @FXML private TableView<Boat> boatList;
    @FXML private TableColumn<Boat, String> boatName;
    @FXML private Label error;
    @FXML private TableColumn<Participants, String> eventBoats;
    @FXML private TableColumn<Participants, Double> eventCost;
    @FXML private TableView<Participants> eventList;
    @FXML private TableColumn<Participants, String> eventName;
    
    private Stage stage;
	private Scene scene;
	private Parent rootParent;
	
	public void initialize(String CF) throws IOException, SQLException, ClassNotFoundException {
		boatName.setCellValueFactory(new PropertyValueFactory<>("name"));
		boatLength.setCellValueFactory(new PropertyValueFactory<>("length"));
		
		Client.os.writeBytes("retrieveBoats#" + CF + "\n");
		Client.os.flush();
		Boat B = (Boat) Client.is.readObject();
		while(B != null){
			boatList.getItems().add(B);
			B = (Boat) Client.is.readObject();
			System.out.println("Ricevuta Una Barca");
		}
		eventName.setCellValueFactory(new PropertyValueFactory<>("eventName"));
		eventCost.setCellValueFactory(new PropertyValueFactory<>("eventCost"));
		eventBoats.setCellValueFactory(new PropertyValueFactory<>("boatName"));
		Client.os.writeBytes("retrieveCompetitions\n");
		Client.os.flush();
		Participants P = (Participants) Client.is.readObject();
		while(P != null){
			eventList.getItems().add(P);
			P = (Participants) Client.is.readObject();
			System.out.println("Ricevuta Una Competizione");
		}
	}
	
	public void eventSubscription() throws IOException {
		try {
			Boat chosenBoat = boatList.getSelectionModel().getSelectedItem();
			Participants chosenEvent = eventList.getSelectionModel().getSelectedItem();
			Client.os.writeBytes(String.format("subscriptEvent#%d#%d\n", chosenEvent.getEventID(), chosenBoat.getID()));
			Client.os.flush();
		} catch (NullPointerException ex) {
			error.setTextFill(Color.DARKRED);
			error.setText("Select a Boat AND a Competition");
			error.setVisible(true);
			PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
			visiblePause.setOnFinished(Event -> error.setVisible(false));
			visiblePause.play();
		}
	}
	
	public void logOut(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIN.fxml"));
		rootParent = loader.load();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(rootParent);
		stage.setScene(scene);
		stage.show();
	}
	
	public void subscript() {
			
	}
	
}
