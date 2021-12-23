package Main;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {	
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("LogIN.fxml"));
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("Sailing Club");
		
		Image iconImage = new Image("Icons/Icon.png");
		primaryStage.getIcons().add(iconImage);
		
		Platform.runLater( () -> root.requestFocus() );
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void main() throws SQLException {
		launch();
	}
}
