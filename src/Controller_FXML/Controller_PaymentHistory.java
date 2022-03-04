package Controller_FXML;

import java.io.IOException;
import java.sql.SQLException;

import Objects.PayIstance;
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

public class Controller_PaymentHistory {
	
	@FXML private TableColumn<PayIstance, String> payMeth;
    @FXML private TableColumn<PayIstance, Double> payAmount;
    @FXML private TableColumn<PayIstance, String> payBoat;
    @FXML private TableColumn<PayIstance, String> payDesc;
    @FXML private TableColumn<PayIstance, String> payExec;
    @FXML private TableView<PayIstance> payHistory;
	
	private Stage stage;
	private Scene scene;
	private Parent rootParent;
	
	private static String Cod_F;

	public void initialize(String CF) throws IOException, SQLException, ClassNotFoundException {
		Cod_F = CF;
		payMeth.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
		payAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
		payBoat.setCellValueFactory(new PropertyValueFactory<>("boatName"));
		payDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
		payExec.setCellValueFactory(new PropertyValueFactory<>("currDate"));
		Client.os.writeBytes("retrievePaymentHistory#" + Cod_F + "\n");
		Client.os.flush();
		PayIstance P = (PayIstance) Client.is.readObject();
		while(P != null){
			payHistory.getItems().add(P);
			P = (PayIstance) Client.is.readObject();
		}
	}
	
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
