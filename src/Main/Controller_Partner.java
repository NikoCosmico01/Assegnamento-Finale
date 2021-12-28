package Main;

import java.io.IOException;
import java.sql.SQLException;

import Socket.Client;
import Vehicle.Boat;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controller_Partner {
	
	@FXML private TableView<Boat> boatList;
	@FXML private TableColumn<Boat, String> Name;
	@FXML private TableColumn<Boat, Integer> ID;
	@FXML private TableColumn<Boat, Double> Length;
	@FXML private TableColumn<Boat, String> Subscription;
	
	
	
	public void initialize(String CF) throws IOException, SQLException, ClassNotFoundException {
		Name.setCellValueFactory(new PropertyValueFactory<>("name"));
		ID.setCellValueFactory(new PropertyValueFactory<>("iD"));
		Length.setCellValueFactory(new PropertyValueFactory<>("length"));
		Subscription.setCellValueFactory(new PropertyValueFactory<>("subscription"));
		
		Client.os.writeBytes("retrieveBoats#" + CF + "\n");
		Client.os.flush();
		
		Boat B = (Boat) Client.is.readObject();
		while(B != null){
			boatList.getItems().add(B);
			B = (Boat) Client.is.readObject();
			System.out.println("Ricevuta una barca");
		}
		
		
	}
	
}
