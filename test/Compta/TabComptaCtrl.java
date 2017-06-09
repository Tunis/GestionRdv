package Compta;

import app.util.RegexUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import metier.action.MCompta;
import metier.action.MMedecin;
import metier.hibernate.data.exceptions.DbCreateException;
import metier.hibernate.data.exceptions.DbSaveException;
import models.Medecin;
import models.compta.ComptaJournaliere;
import models.compta.ComptaMensuelle;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class TabComptaCtrl implements Initializable {


    @FXML
    private Button btnPrec;
    @FXML
    private Button btnSuiv;
    @FXML
    private Button btnCreateCompta;
    @FXML
    private Label lblDate;
    @FXML
    private ComboBox<Medecin> cbMedecin;

    @FXML
    private TableView<ComptaJournaliere> tblComptaJ;
    @FXML
    private TableColumn<ComptaJournaliere, LocalDate> colComptaJDate;
    @FXML
    private TableColumn<ComptaJournaliere, Integer> colComptaJCS;
    @FXML
    private TableColumn<ComptaJournaliere, Integer> colComptaJC2;
    @FXML
    private TableColumn<ComptaJournaliere, Integer> colComptaJECHO;
    @FXML
    private TableColumn<ComptaJournaliere, Integer> colComptaJDIU;
    @FXML
    private TableColumn<ComptaJournaliere, Integer> colComptaJNbTP;
    @FXML
    private TableColumn<ComptaJournaliere, Float> colComptaJTP;
    @FXML
    private TableColumn<ComptaJournaliere, Integer> colComptaJNbEspece;
    @FXML
    private TableColumn<ComptaJournaliere, Float> colComptaJEspece;
    @FXML
    private TableColumn<ComptaJournaliere, Integer> colComptaJNbCB;
    @FXML
    private TableColumn<ComptaJournaliere, Float> colComptaJCB;
    @FXML
    private TableColumn<ComptaJournaliere, Integer> colComptaJNbCheque;
    @FXML
    private TableColumn<ComptaJournaliere, Float> colComptaJCheque;
    @FXML
    private TableColumn<ComptaJournaliere, Float> colComptaJImpayer;
    @FXML
    private TableColumn<ComptaJournaliere, Float> colComptaJSolde;
    @FXML
    private TableColumn<ComptaJournaliere, Float> colComptaJRetrait;

    @FXML
    private TableView<ComptaMensuelle> tblComptaM;

    @FXML
    private TableColumn<ComptaMensuelle, YearMonth> colComptaMDate;

    @FXML
    private TableColumn<ComptaMensuelle, Integer> colComptaMCS;

    @FXML
    private TableColumn<ComptaMensuelle, Integer> colComptaMC2;

    @FXML
    private TableColumn<ComptaMensuelle, Integer> colComptaMECHO;

    @FXML
    private TableColumn<ComptaMensuelle, Integer> colComptaMDIU;

    @FXML
    private TableColumn<ComptaMensuelle, Integer> colComptaMNbTP;

    @FXML
    private TableColumn<ComptaMensuelle, Float> colComptaMTP;

    @FXML
    private TableColumn<ComptaMensuelle, Integer> colComptaMNbEspece;

    @FXML
    private TableColumn<ComptaMensuelle, Float> colComptaMEspece;

    @FXML
    private TableColumn<ComptaMensuelle, Integer> colComptaMNbCB;

    @FXML
    private TableColumn<ComptaMensuelle, Float> colComptaMCB;

    @FXML
    private TableColumn<ComptaMensuelle, Integer> colComptaMNbCheque;

    @FXML
    private TableColumn<ComptaMensuelle, Float> colComptaMCheque;

    @FXML
    private TableColumn<ComptaMensuelle, Float> colComptaMImpayer;

    @FXML
    private TableColumn<ComptaMensuelle, Float> colComptaMSolde;

    @FXML
    private TableColumn<ComptaMensuelle, Float> colComptaMRetrait;

    private MMedecin mMedecin;
    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>(LocalDate.now());
    private MCompta mCompta;
    private ObjectProperty<ComptaMensuelle> comptaMensuelle = new SimpleObjectProperty<>();
    private LocalDate dateCompta;
    private String montantRetrait;

    public void initCompta(MMedecin mMedecin, MCompta mCompta) {
        this.mCompta = mCompta;
        this.mMedecin = mMedecin;
        cbMedecin.itemsProperty().bind(mMedecin.listProperty());
        getCompta();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        date.addListener((observable, oldValue, newValue) -> getCompta());
        btnPrec.setOnAction(e -> date.set(date.get().minusMonths(1)));
        btnSuiv.setOnAction(e -> date.set(date.get().plusMonths(1)));

        comptaMensuelle.addListener((observable, oldValue, newValue) -> showCompta());
        cbMedecin.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> getCompta());


        btnCreateCompta.disableProperty().bind(cbMedecin.getSelectionModel().selectedIndexProperty().lessThanOrEqualTo(-1));


    }

    public void getCompta() {
        // TODO: 02/06/2017
        lblDate.setText(date.get().format(DateTimeFormatter.ofPattern("MMMM y", Locale.getDefault())));
        if (!cbMedecin.getSelectionModel().isEmpty())
            comptaMensuelle.set(mCompta.loadComptaOfMonth(cbMedecin.getItems().get(cbMedecin.getSelectionModel().getSelectedIndex()), date.get()));
    }

    private void showCompta() {
        tblComptaJ.itemsProperty().bind(new ReadOnlyListWrapper<ComptaJournaliere>(
                FXCollections.observableArrayList(
                        comptaMensuelle.get().getComptaJournaliere())));
        tblComptaM.getItems().clear();
        tblComptaM.getItems().add(comptaMensuelle.get());

        // comptaJ cellFactory :
        colComptaJDate.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getDate()));
        colComptaJCS.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getNbCS()));
        colComptaJC2.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getNbC2()));
        colComptaJECHO.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getNbEcho()));
        colComptaJDIU.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getNbDIU()));
        colComptaJNbTP.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getNbTp()));
        colComptaJTP.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getTp()));
        colComptaJNbEspece.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getNbEspece()));
        colComptaJEspece.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getEspece()));
        colComptaJNbCB.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getNbCB()));
        colComptaJCB.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getCB()));
        colComptaJNbCheque.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getNbCheque()));
        colComptaJCheque.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getCheque()));
        colComptaJImpayer.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getImpayer()));
        colComptaJSolde.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getSoldePrecedent()));
        colComptaJRetrait.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getRetrait()));


        colComptaMDate.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getDate()));
        colComptaMCS.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getNbCS()));
        colComptaMC2.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getNbC2()));
        colComptaMECHO.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getNbEcho()));
        colComptaMDIU.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getNbDIU()));
        colComptaMNbTP.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getNbTp()));
        colComptaMTP.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getTp()));
        colComptaMNbEspece.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getNbEspece()));
        colComptaMEspece.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getEspece()));
        colComptaMNbCB.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getNbCB()));
        colComptaMCB.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getCB()));
        colComptaMNbCheque.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getNbCheque()));
        colComptaMCheque.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getCheque()));
        colComptaMImpayer.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getImpayer()));
        colComptaMSolde.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getsolde()));
        colComptaMRetrait.setCellValueFactory(dataCell -> new ReadOnlyObjectWrapper<>(dataCell.getValue().getRetrait()));
    }

    public void createCompta(ActionEvent event) throws IOException {
        // TODO: 05/06/2017 fenetre pour choisir la date et le montant du retrait
        Stage dialog = new Stage();
        dialog.setScene(new Scene(new CreateComptaCtrl()));
        dialog.showAndWait();

        String errorMessage = "";
        float retrait = 0;

        System.out.println("montant : " + montantRetrait);
        if (montantRetrait != null) {
            if (!montantRetrait.isEmpty() && RegexUtil.validateFloatField(montantRetrait)) {
                retrait = Float.parseFloat(montantRetrait);
            } else {
                errorMessage += "le montant du retrait est incorrect.\n";
            }
        }
        if (dateCompta == null) {
            errorMessage += "La date est incorrecte.\n";
        }
        System.out.println("ici : " + errorMessage);
        Medecin medecin = cbMedecin.getItems().get(cbMedecin.getSelectionModel().getSelectedIndex());

        if (errorMessage.isEmpty()) {
            if (mCompta.doesNotExist(medecin, dateCompta)) {
                try {
                    mCompta.createComptaOfDay(medecin, dateCompta, retrait);
                    montantRetrait = null;
                } catch (DbSaveException e) {
                    e.printStackTrace();
                } catch (DbCreateException e) {
                    e.printStackTrace();
                }
                getCompta();
            }
        }
    }

    public class CreateComptaCtrl extends VBox {

        private DatePicker dpDate;
        private TextField textFMontant;


        public void initialize() {
            dpDate.editorProperty().get().textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    dateCompta = dpDate.getValue();
                } catch (Exception ignored) {
                }
            });
            textFMontant.textProperty().addListener((observable, oldValue, newValue) -> {
                montantRetrait = newValue;
            });
        }

        public CreateComptaCtrl() {
            Label lblDate = new Label("date :");
            dpDate = new DatePicker();
            dpDate.setPromptText("Date de la compta");
            dpDate.setEditable(false);
            textFMontant = new TextField();
            textFMontant.setPromptText("montant du retrait");
            VBox.setMargin(lblDate, new Insets(10, 10, 5, 10));
            VBox.setMargin(dpDate, new Insets(5, 10, 10, 10));
            VBox.setMargin(textFMontant, new Insets(10));
            this.setFillWidth(true);
            this.setAlignment(Pos.CENTER);
            this.setSpacing(10);
            this.getChildren().addAll(lblDate, dpDate, textFMontant);
            initialize();
        }
    }
}
