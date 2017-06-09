package print;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import metier.hibernate.DataBase;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Print extends Application {

    public static Stage stage;
    public static BorderPane root;

    public static void main(String[] args) {

//        Printer printer = Printer.getDefaultPrinter();
//
//        System.out.println("default printer : " + printer.getName());
//
//
//        PrinterJob job = PrinterJob.createPrinterJob(printer);

        launch(args);

    }


    public static List<TestItem> generatedItem() {
        List<TestItem> testItems = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int year = ThreadLocalRandom.current().nextInt(2000, 2017);
            int month = ThreadLocalRandom.current().nextInt(1, 13);
            int day = ThreadLocalRandom.current().nextInt(1, 25);
            int hour = ThreadLocalRandom.current().nextInt(0, 24);
            int min = ThreadLocalRandom.current().nextInt(0, 60);
            testItems.add(new TestItem("item " + i, LocalDate.of(year, month, day), LocalTime.of(hour, min)));
        }
        return testItems;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("test.fxml"));
        root = loader.load();
        testCtrl ctrl = loader.getController();
        ctrl.setStage(stage);
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(e -> DataBase.close());
        stage.show();
    }
}
