package Main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import Person.Partner;
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
	
	public void initialize(String CF) throws IOException, SQLException {
		Name.setCellValueFactory(new PropertyValueFactory<>("name"));
		ID.setCellValueFactory(new PropertyValueFactory<>("iD"));
		Length.setCellValueFactory(new PropertyValueFactory<>("length"));
		Subscription.setCellValueFactory(new PropertyValueFactory<>("subscription"));
		
		ArrayList<Boat> B = Link.retrieveBoats(CF);
		for(int i = 0; i < B.size(); i++) {
			boatList.getItems().add(B.get(i));
		}
		
	}
	
}
