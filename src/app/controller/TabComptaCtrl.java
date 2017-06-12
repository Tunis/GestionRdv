package app.controller;

import app.Main;
import app.util.AlerteUtil;
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
import javafx.print.PageOrientation;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import metier.action.MCompta;
import metier.action.MMedecin;
import metier.hibernate.data.exceptions.DbCreateException;
import metier.hibernate.data.exceptions.DbSaveException;
import metier.print.Printable;
import models.Medecin;
import models.compta.ComptaJournaliere;
import models.compta.ComptaMensuelle;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

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
    private TextField cbMedecin;

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
    private Main mainApp;

    private AutoCompletionBinding<Medecin> mAC;

    public void initCompta(Main mainApp1, MMedecin mMedecin, MCompta mCompta) {
        this.mCompta = mCompta;
        this.mMedecin = mMedecin;
        this.mainApp = mainApp1;

        mAC = TextFields.bindAutoCompletion(cbMedecin, mMedecin.getList());
        mAC.setOnAutoCompleted(e -> {
            cbMedecin.setText(e.getCompletion().toString());
            mainApp.medecinProperty().set(e.getCompletion());
        });

        mMedecin.listProperty().addListener((observable, oldValue, newValue) -> {
            mAC.dispose();
            mAC = TextFields.bindAutoCompletion(cbMedecin, mMedecin.getList());
            mAC.setOnAutoCompleted(e -> {
                cbMedecin.setText(e.getCompletion().toString());
                mainApp.medecinProperty().set(e.getCompletion());
            });
        });

        mainApp.medecinProperty().addListener((observable, oldValue, newValue) -> {
            cbMedecin.setText(mainApp.getMedecin().toString());
            getCompta();
        });

        btnCreateCompta.disableProperty().bind(mainApp.medecinProperty().isNull());

        getCompta();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        date.addListener((observable, oldValue, newValue) -> getCompta());
        btnPrec.setOnAction(e -> date.set(date.get().minusMonths(1)));
        btnSuiv.setOnAction(e -> date.set(date.get().plusMonths(1)));

        comptaMensuelle.addListener((observable, oldValue, newValue) -> showCompta());

        tblComptaJ.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    PrinterJob job = PrinterJob.createPrinterJob();
                    job.showPrintDialog(mainApp.getPrimaryStage());
                    Printable.print(job, Printable.createComptaJ(newValue), PageOrientation.LANDSCAPE);
                } catch (Exception ex) {
                    AlerteUtil.showAlerte(mainApp.getPrimaryStage(), "Erreur d'impression", "Probleme lors de l'impression", "une erreur est survenu empechant l'impression de s'effectué.\n Veuillez reessayer, si le probleme persiste contacter l'administrateur.");
                }
            }
        });
        tblComptaJ.setOnMouseClicked(e -> tblComptaJ.getSelectionModel().clearSelection());

        tblComptaM.setOnMouseClicked(e -> {
            if (comptaMensuelle.get() != null) {
                try {
                    PrinterJob job = PrinterJob.createPrinterJob();
                    job.showPrintDialog(mainApp.getPrimaryStage());
                    Printable.print(job, Printable.createComptaM(comptaMensuelle.get()), PageOrientation.PORTRAIT);
                } catch (Exception ex) {
                    AlerteUtil.showAlerte(mainApp.getPrimaryStage(), "Erreur d'impression", "Probleme lors de l'impression", "une erreur est survenu empechant l'impression de s'effectué.\n Veuillez reessayer, si le probleme persiste contacter l'administrateur.");
                }
            }
            tblComptaM.getSelectionModel().clearSelection();
        });
    }

    public void getCompta() {
        lblDate.setText(date.get().format(DateTimeFormatter.ofPattern("MMMM y", Locale.getDefault())));

        if (mainApp.medecinProperty().isNotNull().get()) {
            ComptaMensuelle c2m = mCompta.loadComptaOfMonth(mainApp.medecinProperty().get(), date.get());

            comptaMensuelle.set(c2m);
        }
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
        Stage dialog = new Stage();
        dialog.setScene(new Scene(new CreateComptaCtrl()));
        dialog.showAndWait();

        String errorMessage = "";
        float retrait = 0;

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

        if (errorMessage.isEmpty()) {
            if (mCompta.doesNotExist(mainApp.medecinProperty().get(), dateCompta)) {
                try {
                    mCompta.createComptaOfDay(mainApp.medecinProperty().get(), dateCompta, retrait);
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
            this.setOnKeyReleased(ke -> {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    Stage window = (Stage) this.getScene().getWindow();
                    window.close();
                }
            });
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
