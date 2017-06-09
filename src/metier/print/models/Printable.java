package metier.print.models;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import metier.hibernate.data.DataCompta;
import metier.hibernate.data.exceptions.DbGetException;
import models.Medecin;
import models.Patient;
import models.PresentDay;
import models.Rdv;
import models.compta.ComptaJournaliere;
import models.enums.TypeRdv;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class Printable {


    public static VBox createPlanning(Medecin medecin, LocalDate date) {
        Label lblDate = new Label("Date : ");
        Label lblDay = new Label(date.format(DateTimeFormatter.ofPattern("EEEE dd MMMM u", Locale.getDefault())));
        Label lblMedecin = new Label("Medecin : ");
        Label lblNom = new Label(medecin.toString());
        HBox hbDate = new HBox(lblDate, lblDay);
        hbDate.setAlignment(Pos.CENTER);
        HBox hbMedecin = new HBox(lblMedecin, lblNom);
        hbMedecin.setAlignment(Pos.CENTER);

        TableColumn<Rdv, LocalTime> colTime = new TableColumn<>("Heure");
        TableColumn<Rdv, TypeRdv> colType = new TableColumn<>("Type rdv");
        TableColumn<Rdv, String> colCot = new TableColumn<>("Cotation");
        TableColumn<Rdv, Patient> colP = new TableColumn<>("Patient");
        TableColumn<Rdv, String> colNom = new TableColumn<>("Nom");
        TableColumn<Rdv, String> colNomJF = new TableColumn<>("Nom JF");
        TableColumn<Rdv, String> colPrenom = new TableColumn<>("Prénom");

        colTime.setCellFactory(p -> {
            TableCell<Rdv, LocalTime> tc = new TableCell<Rdv, LocalTime>() {
                @Override
                public void updateItem(LocalTime item, boolean empty) {
                    if (item != null) {
                        setText(item.toString());
                    }
                }
            };
            tc.setAlignment(Pos.TOP_RIGHT);
            return tc;
        });
        colTime.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getTime()));
        colTime.setMaxWidth(75);
        colTime.setMinWidth(75);
        colCot.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getCotation()));
        colCot.setMaxWidth(75);
        colCot.setMinWidth(75);
        colNom.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getPatient().getLastName() != null ?
                data.getValue().getPatient().getLastName() : ""));
        colNomJF.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getPatient().getMaidenName()));
        colPrenom.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getPatient().getFirstName()));
        colP.getColumns().addAll(colNom, colNomJF, colPrenom);

        TableView<Rdv> rdvList;
        Optional<PresentDay> planning = medecin.getPlannings().stream().filter(p -> p.getPresent().equals(date)).findFirst();
        if (planning.isPresent()) {
            List<Rdv> rdvs = planning.get().getRdvList();
            rdvs.sort(Rdv::compareTo);
            rdvList = new TableView<>(FXCollections.observableArrayList(rdvs));
        } else {
            rdvList = new TableView<>();
        }
        rdvList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        rdvList.setMaxHeight(Integer.MAX_VALUE);
        rdvList.getColumns().addAll(colTime, colP, colCot);
        rdvList.setFixedCellSize(18);


        VBox printable = new VBox(hbDate, hbMedecin, rdvList);
        printable.getStylesheets().add(Printable.class.getResource("/css/print.css").toExternalForm());
        printable.setSpacing(10);
        printable.setMaxHeight(Integer.MAX_VALUE);
        VBox.setVgrow(rdvList, Priority.ALWAYS);
        printable.setAlignment(Pos.TOP_CENTER);
        return printable;
    }

    public static BorderPane createComptaJ(Medecin medecin, LocalDate date) throws IOException {

        DataCompta db = new DataCompta();
        ComptaJournaliere comptaOfDay = null;
        try {
            comptaOfDay = db.getComptaOfDay(medecin, date);
        } catch (DbGetException e) {
            e.printStackTrace();
        }
        if (comptaOfDay != null)
            return createComptaJ(comptaOfDay);
        else return null;
    }

    public static BorderPane createComptaJ(ComptaJournaliere cj) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Printable.class.getResource("planning.fxml"));
        BorderPane printable = loader.load();
        PlanningCtrl ctrl = loader.getController();
        ctrl.initData(cj);

        return printable;
    }
}
