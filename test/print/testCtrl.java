package print;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import metier.action.MMedecin;
import metier.print.models.Printable;
import models.Medecin;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class testCtrl implements Initializable {


    public ListView<TestItem> list;
    public TableView<TestItem> table;
    public TableColumn<TestItem, String> col1;
    public TableColumn<TestItem, String> col2;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<TestItem> observableList1 = FXCollections.observableArrayList(Print.generatedItem());

        list.setCellFactory(new Callback<ListView<TestItem>, ListCell<TestItem>>() {
            @Override
            public ListCell<TestItem> call(ListView<TestItem> param) {
                return new ListCell<TestItem>() {
                    @Override
                    protected void updateItem(TestItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getNom());
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });

        list.setItems(observableList1);

        table.setItems(observableList1);

        col1.setCellValueFactory(data -> {
            StringBuilder sb = new StringBuilder();
            sb.append(data.getValue().getDate()).append(" à ").append(data.getValue().getTime());
            return new ReadOnlyStringWrapper(sb.toString());
        });
        col2.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getNom()));


    }

    public void print(ActionEvent event) {
        MMedecin mMedecin = new MMedecin();
        Medecin medecin = mMedecin.getList().get(0);
        LocalDate date = LocalDate.of(2017, 6, 9);

        try {
            print(medecin, date);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void print(Medecin medecin, LocalDate date) throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("planning.fxml"));
//        VBox printable = loader.load();
//        PlanningCtrl ctrl = loader.getController();
//
//        ctrl.loadData(stage, printable, medecin, date);
//        ctrl.print();

        PrinterJob job = PrinterJob.createPrinterJob();
        job.showPrintDialog(stage);

        PageLayout pageLayout = job.getPrinter().createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, 50, 50, 0.5, 0);

        job.getJobSettings().setPageLayout(pageLayout);

        double pH = pageLayout.getPrintableHeight();
        double pW = pageLayout.getPrintableWidth();

        //VBox printable = Printable.createPlanning(medecin, date);
        BorderPane printable = Printable.createComptaJ(medecin, date);
        printable.setMinSize(pW, pH);
        printable.resize(pW, pH);

        Stage stage = new Stage();
        stage.setScene(new Scene(printable));
        stage.show();

        try {
            boolean b = job.printPage(printable);
            if (b) {
                job.endJob();
            } else {
                System.out.println("erreur?");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
