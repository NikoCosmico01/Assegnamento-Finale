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
import Objects.Notification;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller_Notification {

    @FXML private TableColumn<Notification, Double> Amount;
    @FXML private TableColumn<Notification, String> Description;
    @FXML private TableColumn<Notification, String> objectString;
    @FXML private TableView<Notification> notificationHistory;
    @FXML private TableColumn<Notification, Integer> remDays;
	@FXML private Label error;
	
	private static String Cod_F;
	private Stage stage;
	private Scene scene;
	private Parent rootParent;
	
	public void initialize() throws ClassNotFoundException, IOException {
		objectString.setCellValueFactory(new PropertyValueFactory<>("object"));
		Description.setCellValueFactory(new PropertyValueFactory<>("description"));
		remDays.setCellValueFactory(new PropertyValueFactory<>("days"));
		Amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
		//TODO
		Notification N = (Notification) Client.is.readObject();
		while(N != null){
			notificationHistory.getItems().add(N);
			N = (Notification) Client.is.readObject();
		}
	}
	
	public void pay(ActionEvent event) throws IOException, ClassNotFoundException{
		try {
			String desctription;
			String amount;
			String boatLenght;
			Notification chosenNotification = notificationList.getSelectionModel().getSelectedItem();
			desctription = chosenNotification.getDescription();
			amount = chosenNotification.getAmount();
			boatLenght = chosenNotification.getBoatLenght();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Pay.fxml"));
			rootParent = loader.load();
			Controller_Pay Pay = loader.getController();
			Client.os.writeBytes("retrievePerson#" + Cod_F + "\n");
			Client.os.flush();
			Person P = (Person) Client.is.readObject();
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(rootParent);
			Pay.initialize(P, "boatFee", Double.parseDouble(amount), desctription + "#"+ boatLenght, stage, scene);
		} catch (NullPointerException ex) {
			error.setTextFill(Color.DARKRED);
			error.setText("Select A Notification");
			error.setVisible(true);
			PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
			visiblePause.setOnFinished(Event -> error.setVisible(false));
			visiblePause.play();
		}
		
		
	}
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
	
	public void initialize(String CF) {
    	Cod_F = CF;
    }
}