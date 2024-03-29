package Controller_FXML;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import Objects.Boat;
import Objects.Message;
import Objects.Participant;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This class is the most important, it contains all the logic in the user homepage.
 * The user can add,remove boats, subscribe/unsubscribe from competitions, check if he has won a competition etc.
 * 
 * @author NicoT
 *
 */

public class Controller_Partner {

	@FXML private TableColumn<Boat, Double> boatLength;
	@FXML private TableView<Boat> boatList;
	@FXML private TableColumn<Boat, String> boatName;
	@FXML private Label error;
	@FXML private TableColumn<Participant, String> eventBoats;
	@FXML private TableColumn<Participant, Double> eventCost;
	@FXML private TableView<Participant> eventList;
	@FXML private TableColumn<Participant, String> eventName;
	@FXML private Circle notificationPopUp;

	private Stage stage;
	private Scene scene;
	private Parent rootParent;

	private static String Cod_F;

	/**
	 * This main class has the purpose to populate both the boat and competition tableviews and check if any notification has to be displayed.
	 * 
	 * @param CF passed Fiscal Code
	 * @throws IOException Handles Input-Output Exceptions
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 */
	
	public void initialize(String CF) throws IOException, SQLException, ClassNotFoundException {
		boatName.setCellValueFactory(new PropertyValueFactory<>("name"));
		boatLength.setCellValueFactory(new PropertyValueFactory<>("length"));

		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());

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
		Client.os.writeBytes("retrieveCompetitions#" + CF + "\n");
		Client.os.flush();
		Participant P = (Participant) Client.is.readObject();
		ArrayList<Participant> participantsList = new ArrayList<Participant>();
		ArrayList<String> mylist = new ArrayList<String>();
		while(P != null){
			participantsList.add(P);
			eventList.getItems().add(P);
			P = (Participant) Client.is.readObject();
		}

		Participant P1;
		for (Integer x = 0; x < participantsList.size(); x++) {
			P1 = participantsList.get(x);
			if (formatter.format(date).equals(P1.getEventDate()) && P1.getEventPodium() == null) {
				Client.os.writeBytes("getAllParticipants#" + P1.getEventID() + "\n");
				Client.os.flush();
				Message M = (Message) Client.is.readObject();
				while (M != null) {
					mylist.add(M.getMsg());
					M = (Message) Client.is.readObject();
				}
				Collections.shuffle(mylist);
				String podiumString = "";
				for (Integer i = 0; i < mylist.size(); i++) {
					if (i == mylist.size()-1) {
						podiumString += mylist.get(i);
					} else {
						podiumString += mylist.get(i) + "-";
					}
					Client.os.writeBytes("setPodium#" + P1.getEventID() + "#" + podiumString + "\n");
					Client.os.flush();
				}
			}
		}
		Client.os.writeBytes("checkNotifications#" + Cod_F + "\n");
		Client.os.flush();
		if (Client.is.readByte() == 1) {
			notificationPopUp.setVisible(true);
		}		
	}

	/**
	 * This method is invoked when the "ADD BOAT" button is pressed, it redirects the user to a new Page in order to add a new boat
	 * and pay the requested fee.
	 * 
	 * @param event  GUI Click Event
	 * @throws IOException Handles Input-Output Exceptions
	 */
	@FXML
	void addBoat(ActionEvent event) throws IOException {
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

	/**
	 * This method is invoked when a boat is selected and the "REMOVE BOAT" button is pressed, it also checks if that boat is subscripted to a running competition
	 * and, if yes, it sends an alert.
	 * 
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 * @throws SQLException Handles SQL Errors
	 */
	@FXML
	void removeBoat() throws ClassNotFoundException, IOException, SQLException {
		try {
			Boat chosenBoat = boatList.getSelectionModel().getSelectedItem();
			Integer boatID = chosenBoat.getID();
			Client.os.writeBytes("removeBoat#" + boatID + "#0\n");
			Client.os.flush();
			Message M = (Message) Client.is.readObject();
			if (M.getMsg().equals("OK")) {
				error.setTextFill(Color.DARKGREEN);
				error.setText("Boat Successfully Removed");
				error.setVisible(true);
				PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
				visiblePause.setOnFinished(Event -> error.setVisible(false));
				visiblePause.play();
			} else if (M.getMsg().equals("RQDL")) {
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

	/**
	 * This method is invoked when a competition is selected and the "DELETE SUBSCRIPTION" button is pressed in order to 
	 * delete that boat from the competition.
	 * 
	 * @param event  GUI Click Event
	 * @throws IOException Handles Input-Output Exceptions
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws SQLException Handles SQL Errors
	 */
	@FXML
	void deleteSubscription(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		try {
			Participant chosenEvent = eventList.getSelectionModel().getSelectedItem();
			if (chosenEvent.getBoatID() == null) {
				Client.os.writeBytes(String.format("deleteSubscription#%d#%d\n", chosenEvent.getEventID(), 0));
			} else {
				Client.os.writeBytes(String.format("deleteSubscription#%d#%d\n", chosenEvent.getEventID(), chosenEvent.getBoatID()));
			}
			Client.os.flush();
			Message M = (Message) Client.is.readObject();
			if (M.getMsg().equals("OK")) {
				error.setTextFill(Color.DARKGREEN);
				error.setText("Subscription Successfully Deleted");
				error.setVisible(true);
				PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
				visiblePause.setOnFinished(Event -> error.setVisible(false));
				visiblePause.play();
			} else if (M.getMsg().equals("NotSub")) {
				error.setTextFill(Color.DARKRED);
				error.setText("Competition NOT Subscripted");
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
			error.setText("Select a Competition");
			error.setVisible(true);
			PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
			visiblePause.setOnFinished(Event -> error.setVisible(false));
			visiblePause.play();
		}
		boatList.getItems().clear();
		eventList.getItems().clear();
		initialize(Cod_F);
	}

	/**
	 * This method is called when both a boat and a competition have been selected and the "SUBSCRIBE" button is pressed.
	 * 
	 * @param event  GUI Click Event
	 * @throws IOException Handles Input-Output Exceptions
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws SQLException Handles SQL Errors
	 */
	@FXML
	void eventSubscription(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		try {
			Boat chosenBoat = boatList.getSelectionModel().getSelectedItem();
			Participant chosenEvent = eventList.getSelectionModel().getSelectedItem();
			Client.os.writeBytes(String.format("checkEvent#" + chosenEvent.getEventID() + "#" + chosenBoat.getID() + "#" + Cod_F + "\n"));
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
				Pay.initialize(P, "compFee", chosenEvent.getEventCost(), chosenBoat.getID() + "#" + chosenEvent.getEventID(), stage, scene, 0);
			} else if (M.getMsg().equals("CSE")) {
				error.setTextFill(Color.DARKRED);
				error.setText("Competition Still Subscripted");
				error.setVisible(true);
				PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
				visiblePause.setOnFinished(Event -> error.setVisible(false));
				visiblePause.play();
			} else if (M.getMsg().equals("BSE")) {
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
	}

	/**
	 * This method is called when the "SUBSCRIPTION MANAGEMENT" button is pressed in order to show the user more details on the subscribed competitions.
	 * 
	 * @param event GUI Click Event
	 * @throws IOException Handles Input-Output Exceptions
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws SQLException Handles SQL Errors
	 */
	@FXML
	void subscriptionManagement(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("History.fxml"));
		rootParent = loader.load();
		Controller_History History = loader.getController();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(rootParent);
		History.initialize(Cod_F);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * This method is called when the "PAYMENT HISTORY" button is pressed in order to show the user all his payment history and
	 * relative details.
	 * 
	 * @param event  GUI Click Event
	 * @throws IOException Handles Input-Output Exceptions
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws SQLException Handles SQL Errors
	 */
	@FXML
	void paymentHistory(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PaymentHistory.fxml"));
		rootParent = loader.load();
		Controller_PaymentHistory History = loader.getController();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(rootParent);
		History.initialize(Cod_F);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * This method is called then the "LOGOUT" button is pressed, it logs out from the system and returns the user to the initial login page
	 * in order to start again.
	 * 
	 * @param event GUI Click Event
	 * @throws IOException Handles Input-Output Exceptions
	 */
	@FXML
	void logOut(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIN.fxml"));
		rootParent = loader.load();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(rootParent);
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * This method is called when the "NOTIFICATION BUTTON" is pressed and enter the page when you can see all the notifications.
	 * 
	 * @param event GUI Click Event
	 * @throws IOException Handles Input-Output Exceptions
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 */
	@FXML
	void Notification(ActionEvent event) throws IOException, ClassNotFoundException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Notification.fxml"));
		rootParent = loader.load();
		Controller_Notification notificationController = loader.getController();
		notificationController.initialize(Cod_F);
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(rootParent);
		stage.setScene(scene);
		stage.show();
	}
}
