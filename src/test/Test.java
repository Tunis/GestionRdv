package test;


import app.view.custom.CalendarMonth;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import metier.hibernate.data.DataMedecin;
import models.Medecin;

import java.util.List;

public class Test extends Application {

    private DataMedecin db = new DataMedecin();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        List<Medecin> all = db.getAll();
        ObservableList<Medecin> medecins = FXCollections.observableArrayList(all);
        medecins.sort(Medecin::compareTo);

        BorderPane borderPane = new BorderPane();

        CalendarMonth calendarMonth = new CalendarMonth();
        ComboBox<Medecin> cbMedecin = new ComboBox<>(medecins);

        borderPane.setTop(cbMedecin);
        borderPane.setCenter(calendarMonth);

        primaryStage.setScene(new Scene(borderPane));
        primaryStage.show();

        calendarMonth.itemProperty().bind(cbMedecin.getSelectionModel().selectedItemProperty());
    }
}
