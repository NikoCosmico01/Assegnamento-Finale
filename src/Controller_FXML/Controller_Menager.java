package Controller_FXML;
import java.io.IOException;
import Socket.Client;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
public class Controller_Menager {
	@FXML private TextField NameEvent;
    @FXML private TextField Max;
    @FXML private TextField Price;
    @FXML private TextField Amount;
    @FXML private TableView<Competition> Events;
    @FXML private TextField userNameText;
    @FXML private Label error;
}
