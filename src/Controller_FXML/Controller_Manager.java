package Controller_FXML;

import java.io.IOException;
import java.time.LocalDate;

import Objects.Boat;
import Objects.Participant;
import Socket.Client;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This class has the purpose to manage the Manager GUI, it populates the tableview and allows the manager to delete or add a competition.
 * 
 * @author NicoT
 *
 */

public class Controller_Manager {

	@FXML private TableColumn<Boat, Double> boatLength;
	@FXML private TableColumn<Boat, String> boatName;
	@FXML private TableColumn<Boat, String> boatOwner;
	@FXML private TableColumn<Participant, String> eventDate;
	@FXML private TableColumn<Participant, String> eventName;
	@FXML private TableColumn<Participant, Double> eventPrice;
	@FXML private TableColumn<Participant, Double> eventSubAmount;
	@FXML private TableView<Participant> eventList;
	@FXML private TableView<Boat> infoList;
	@FXML private Label error;
	@FXML private DatePicker newEventDate;
    @FXML private TextField newEventName;
    @FXML private TextField newEventPrice;
    @FXML private TextField newEventSubAmount;

	private Stage stage;
	private Scene scene;
	private Parent rootParent;

	/**
	 * This is the first method that is called when a manager logs in, its purpose is to fill in the tableview with all the
	 * existing competitions.
	 * 
	 * @throws IOException Handles Input-Output Exceptions
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 */
	
	public void initialize() throws IOException, ClassNotFoundException {
		eventName.setCellValueFactory(new PropertyValueFactory<>("eventName"));
		eventPrice.setCellValueFactory(new PropertyValueFactory<>("eventPrize"));
		eventDate.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
		eventSubAmount.setCellValueFactory(new PropertyValueFactory<>("eventCost"));
		Client.os.writeBytes("getEvents#0\n");
		Client.os.flush();
		Participant P = (Participant) Client.is.readObject();
		while(P != null){
			eventList.getItems().add(P);
			P = (Participant) Client.is.readObject();
		}
		LocalDate minDate = LocalDate.now();
		newEventDate.setDayCellFactory(d ->
        	new DateCell() {
        		@Override public void updateItem(LocalDate item, boolean empty) {
        			super.updateItem(item, empty);
        			setDisable(item.isBefore(minDate));
        		}});
	}

	/**
	 * This method is called when the "Add Event" button is pressed, it checks if all the fields have been correctly filled and, if yes,
	 * if proceed to adding the newly inserted competition into the database.
	 * 
	 * @throws IOException Handles Input-Output Exceptions
	 */
	@FXML
	private void addEvent() throws IOException {
		String Name = newEventName.getText();
		String prizeString = newEventPrice.getText();
		String costString = newEventSubAmount.getText();
		if (Name.isBlank() || prizeString.isBlank() || costString.isBlank()) {
			error.setTextFill(Color.DARKRED);
			error.setText("Fill All The Fields");
			error.setVisible(true);
			PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
			visiblePause.setOnFinished(Event -> error.setVisible(false));
			visiblePause.play();
		} else {
			LocalDate localDate = newEventDate.getValue();
			Client.os.writeBytes("createEvent#" +  Name + "#" + prizeString + "#" + costString + "#" + localDate +"\n");
			Client.os.flush();
			error.setTextFill(Color.DARKGREEN);
			error.setText("Event Correctly Added");
			error.setVisible(true);
			PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
			visiblePause.setOnFinished(Event -> error.setVisible(false));
			visiblePause.play();
		}
	}

	/**
	 * This method is called when the remove button is pressed; then the system will remove the selected competition from the database but,
	 * if no competition is selected, it will show an error.
	 * 
	 * @param event GUI Click Event
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */
	@FXML
	private void Remove(ActionEvent event) throws IOException, ClassNotFoundException {
		try {
			Participant P = eventList.getSelectionModel().getSelectedItem();
			System.out.println(P.getEventID());
			Client.os.writeBytes("deleteCompetition#" + P.getEventID() + "\n");
			Client.os.flush();
		} catch (NullPointerException ex) {
			error.setTextFill(Color.DARKRED);
			error.setText("Select A Competition");
			error.setVisible(true);
			PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
			visiblePause.setOnFinished(Event -> error.setVisible(false));
			visiblePause.play();
		}
		eventList.getItems().clear();
		initialize();
	}

	/**
	 * This method is called then the "LOGOUT" button is pressed, it logs out from the system and returns the user to the initial login page
	 * in order to start again.
	 * 
	 * @param event GUI Click Event
	 * @throws IOException Handles Input-Output Exceptions
	 */
	@FXML
	private void logOut(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIN.fxml"));
		rootParent = loader.load();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(rootParent);
		stage.setScene(scene);
		stage.show();
	}

}
