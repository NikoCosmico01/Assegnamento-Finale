package Main;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

/**
 * This class is the Main one, from which the all program gui starts.
 * 
 * @author NicoT
 *
 */

public class Main extends Application {	
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/Controller_FXML/LogIN.fxml"));
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("Sailing Club");
		
		Image iconImage = new Image("Icons/Icon.png");
		primaryStage.getIcons().add(iconImage);
		
		Platform.runLater( () -> root.requestFocus() );
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Initial Launch method.
	 * 
	 * @param args Passed Arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
