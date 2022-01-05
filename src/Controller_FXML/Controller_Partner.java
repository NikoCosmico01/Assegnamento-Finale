package Controller_FXML;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import Event.Participants;
import Socket.Client;
import Socket.Server;
import Vehicle.Boat;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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
	
	private static String Cod_F;
	
	public void initialize(String CF) throws IOException, SQLException, ClassNotFoundException {
		boatName.setCellValueFactory(new PropertyValueFactory<>("name"));
		boatLength.setCellValueFactory(new PropertyValueFactory<>("length"));
		
		Cod_F = CF;
		Client.os.writeBytes("retrieveBoats#" + CF + "\n");
		Client.os.flush();
		Boat B = (Boat) Client.is.readObject();
		while(B != null){
			boatList.getItems().add(B);
			B = (Boat) Client.is.readObject();
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
		}
	}
	
	public void addBoat(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AddBoat.fxml"));
		rootParent = loader.load();
		Controller_AddBoat AddBoat = loader.getController();
		AddBoat.initialize(Cod_F);
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(rootParent);
		stage.setScene(scene);
		Platform.runLater( () -> rootParent.requestFocus() );
		stage.show();
	}
	
	public void removeBoat() throws ClassNotFoundException, IOException, SQLException {
		try {
			Boat chosenBoat = boatList.getSelectionModel().getSelectedItem();
			Integer boatID = chosenBoat.getID();
			Client.os.writeBytes("removeBoat#" + boatID + "#0\n");
			Client.os.flush();
			String returString = Client.is.readLine();
			if (returString.equals("OK")) {
				error.setTextFill(Color.DARKGREEN);
				error.setText("Boat Successfully Removed");
				error.setVisible(true);
				PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
				visiblePause.setOnFinished(Event -> error.setVisible(false));
				visiblePause.play();
			} else if (returString.equals("RQDL")) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("[SERVER] Confirmation Request");
				alert.setHeaderText("The chosen Boat is actually Subscripted to a running competition !");
				alert.setContentText("Do you still want to Delete It ?");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					Client.os.writeBytes("removeBoat#" + boatID + "#1\n");
					Client.os.flush();
				} else {
					error.setTextFill(Color.DARKRED);
					error.setText("Deleted Canceled");
					error.setVisible(true);
					PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
					visiblePause.setOnFinished(Event -> error.setVisible(false));
					visiblePause.play();
					return;
				}
			} else {
				error.setTextFill(Color.DARKRED);
				error.setText("Generic Error");
				error.setVisible(true);
				PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
				visiblePause.setOnFinished(Event -> error.setVisible(false));
				visiblePause.play();
			}
		} catch (NullPointerException ex) {
			error.setTextFill(Color.DARKRED);
			error.setText("Select a Boat");
			error.setVisible(true);
			PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
			visiblePause.setOnFinished(Event -> error.setVisible(false));
			visiblePause.play();
		}
		boatList.getItems().clear();
		eventList.getItems().clear();
		initialize(Cod_F);
	}
	
	@SuppressWarnings("deprecation")
	public void eventSubscription(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		try {
			Boat chosenBoat = boatList.getSelectionModel().getSelectedItem();
			Participants chosenEvent = eventList.getSelectionModel().getSelectedItem();
			Client.os.writeBytes(String.format("subscriptEvent#%d#%d\n", chosenEvent.getEventID(), chosenBoat.getID()));
			Client.os.flush();
			String ackString = Client.is.readLine();
			if (ackString.equals("OK")) {
				error.setTextFill(Color.DARKGREEN);
				error.setText("Boat Successfully Subscripted");
				error.setVisible(true);
				PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
				visiblePause.setOnFinished(Event -> error.setVisible(false));
				visiblePause.play();
			} else if (ackString.equals("CSE")) {
				error.setTextFill(Color.DARKRED);
				error.setText("Competition Still Subscripted");
				error.setVisible(true);
				PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
				visiblePause.setOnFinished(Event -> error.setVisible(false));
				visiblePause.play();
			} else if (ackString.equals("BSE")) {
				error.setTextFill(Color.DARKRED);
				error.setText("Boat Still Subscripted");
				error.setVisible(true);
				PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
				visiblePause.setOnFinished(Event -> error.setVisible(false));
				visiblePause.play();
			} else {
				error.setTextFill(Color.DARKRED);
				error.setText("Generic Error");
				error.setVisible(true);
				PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
				visiblePause.setOnFinished(Event -> error.setVisible(false));
				visiblePause.play();
			}
		} catch (NullPointerException ex) {
			error.setTextFill(Color.DARKRED);
			error.setText("Select a Boat AND a Competition");
			error.setVisible(true);
			PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
			visiblePause.setOnFinished(Event -> error.setVisible(false));
			visiblePause.play();
		}
		boatList.getItems().clear();
		eventList.getItems().clear();
		initialize(Cod_F);
	}
	
	public void logOut(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIN.fxml"));
		rootParent = loader.load();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(rootParent);
		stage.setScene(scene);
		stage.show();
	}
	
}
