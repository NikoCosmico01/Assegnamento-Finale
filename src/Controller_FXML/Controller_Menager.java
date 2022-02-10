package Controller_FXML;

import Competition.Participants;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class Controller_Menager {
	@FXML private TextField NameEvent;
    @FXML private TextField Max;
    @FXML private TextField Price;
    @FXML private TextField Amount;
    @FXML private TableView<Participants> Events;
    @FXML private TextField userNameText;
    @FXML private Label error;
}
