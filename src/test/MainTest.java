package test;
import java.util.Locale;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import metier.hibernate.DataBase;

public class MainTest extends Application {

    @Override
    public void start(Stage primaryStage) {
        CalendarTest calendarView = new CalendarTest() ;

        Button next = new Button(">");
        next.setOnAction(e -> calendarView.dateProperty().set(calendarView.getDate().plusMonths(1)));

        Button previous = new Button("<");
        previous.setOnAction(e -> calendarView.dateProperty().set(calendarView.getDate().minusMonths(1)));


        //calendarView.localeProperty().bind(localeCombo.valueProperty());

        BorderPane root = new BorderPane(calendarView.getView(), null, next, null, previous);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> DataBase.close());
        primaryStage.show();
    }

    public static class LocaleCell extends ListCell<Locale> {
        @Override
        public void updateItem(Locale locale, boolean empty) {
            super.updateItem(locale, empty);
            setText(locale == null ? null : locale.getDisplayName(locale));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}