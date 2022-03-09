package Controller_FXML;

import java.io.IOException;
import java.sql.SQLException;

import Objects.Participant;
import Socket.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * This class contains all the method useful to show all the payment history,
 * when the page is open a tableview is filled with all the past payments completed.
 * 
 * @author NicoT
 *
 */

public class Controller_History {
	
    @FXML private TableColumn<Participant, Integer> eventBoats;
    @FXML private TableColumn<Participant, String> eventDate;
    @FXML private TableView<Participant> eventList;
    @FXML private TableColumn<Participant, String> eventName;
    @FXML private TableColumn<Participant, String> eventPodium;
    @FXML private TableColumn<Participant, Double> eventPrize;
	
	private Stage stage;
	private Scene scene;
	private Parent rootParent;
	
	private static String Cod_F;

	/**
	 * This method is the first that is called when the new page appears; its purpose is to fill in the tableview
	 * with all the values retrieved from the database.
	 * 
	 * @param CF Actual User Fiscal Code
     * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */
	
	public void initialize(String CF) throws IOException, SQLException, ClassNotFoundException {
		Cod_F = CF;
		eventName.setCellValueFactory(new PropertyValueFactory<>("eventName"));
		eventBoats.setCellValueFactory(new PropertyValueFactory<>("boatName"));
		eventDate.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
		eventPrize.setCellValueFactory(new PropertyValueFactory<>("eventPrize"));
		eventPodium.setCellValueFactory(new PropertyValueFactory<>("eventPodium"));
		Client.os.writeBytes("retrieveCompetitions#" + Cod_F + "\n");
		Client.os.flush();
		Participant P = (Participant) Client.is.readObject();
		while(P != null){
			eventList.getItems().add(P);
			P = (Participant) Client.is.readObject();
		}
	}
	
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
	
}
