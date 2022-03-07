package Controller_FXML;

import java.io.IOException;
import java.sql.SQLException;

import Objects.Notification;
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

	public void initialize(String CF) throws ClassNotFoundException, IOException {
		Cod_F = CF;
		objectString.setCellValueFactory(new PropertyValueFactory<>("objectString"));
		Description.setCellValueFactory(new PropertyValueFactory<>("description"));
		remDays.setCellValueFactory(new PropertyValueFactory<>("days"));
		Amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
		Client.os.writeBytes("getNotifications#0\n");
		Client.os.flush();
		Notification N = (Notification) Client.is.readObject();
		while(N != null){
			notificationHistory.getItems().add(N);
			N = (Notification) Client.is.readObject();
		}
	}

	public void Pay(ActionEvent event) throws IOException, ClassNotFoundException{
		try {
			Notification chosenNotification = notificationHistory.getSelectionModel().getSelectedItem();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Pay.fxml"));
			rootParent = loader.load();
			Controller_Pay Pay = loader.getController();
			Client.os.writeBytes("retrievePerson#" + Cod_F + "\n");
			Client.os.flush();
			Person P = (Person) Client.is.readObject();
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(rootParent);
			if (chosenNotification.getObjectString().equals("Boat Addon")) {
				System.out.println("Ecco Boat");
				Pay.initialize(P, "boatFee", chosenNotification.getAmount(),  chosenNotification.getID_Boat() + "#"+ chosenNotification.getLength(), stage, scene, 1);
			} else if (chosenNotification.getObjectString().equals("Membership Registration")) {
				Pay.initialize(null, "membFee", 109.90, P.getName() + "#" + P.getSurname() + "#" + P.getAddress() + "#" + P.getCF() + "#" + P.getUserName() + "#" + P.getPassWord(), stage, scene, 1);
			}
		} catch (NullPointerException ex) {
			error.setTextFill(Color.DARKRED);
			error.setText("Select A Notification");
			error.setVisible(true);
			PauseTransition visiblePause = new PauseTransition(Duration.seconds(1.5));
			visiblePause.setOnFinished(Event -> error.setVisible(false));
			visiblePause.play();
		}
	}

	@FXML
	private void Back(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
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