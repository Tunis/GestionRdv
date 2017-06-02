package Compta;

import app.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import metier.action.MCompta;
import metier.action.MMedecin;
import metier.hibernate.DataBase;

import java.io.IOException;

public class Test extends Application {
    BorderPane comptaOverview;
    ComptaCtrl controller;
    @Override
    public void start(Stage primaryStage) throws Exception {
        MMedecin mMedecin = new MMedecin();
        MCompta mCompta = new MCompta();
        primaryStage.setOnCloseRequest(e-> DataBase.close());
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("compta.fxml"));

            comptaOverview = (BorderPane) loader.load();
            controller = loader.getController();
            controller.initCompta(mMedecin, mCompta);

        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setScene(new Scene(comptaOverview));
        primaryStage.show();
    }
}
