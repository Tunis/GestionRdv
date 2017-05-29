package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import metier.hibernate.DataBase;

public class Gui extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/root.fxml"));
        AnchorPane root = loader.load();

        primaryStage.setOnCloseRequest(event -> {
            DataBase.close();
        });

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
