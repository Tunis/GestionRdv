package autoCompletion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class Ac extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("ac.fxml"));
		TabPane root = loader.load();

		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}
}
