package app.controller;

import app.Main;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import metier.action.MMedecin;
import models.Medecin;
import models.Tp;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TabTpCtrl implements Initializable {


	@FXML
	private Button btnPrec;
	@FXML
	private Label lblDate;
	@FXML
	private Button btnSuiv;
	@FXML
    private TextField cbMedecin;
    @FXML
	private TableView<Tp> tblTp;
	@FXML
	private TableColumn<Tp, String> colDate;
	@FXML
	private TableColumn<Tp, String> colNom;
	@FXML
	private TableColumn<Tp, String> colPrenom;
	@FXML
	private TableColumn<Tp, Float> colMontant;
	@FXML
	private TableColumn<Tp, String> colRegler;

	private ObjectProperty<YearMonth> dateProperty = new SimpleObjectProperty<>(YearMonth.now());
	private MMedecin mMedecin;
    private Main mainApp;
    private AutoCompletionBinding<Medecin> mAC;

    public void initController(Main mainApp, MMedecin mMedecin) {
        this.mMedecin = mMedecin;
        this.mainApp = mainApp;
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
        setListiner();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		lblDate.setText(dateProperty.get().format(DateTimeFormatter.ofPattern("MM/uuuu")));

		colDate.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPaiement().getRdv().getPresentDay().getPresent().format(DateTimeFormatter.ofPattern("dd/MM/uuuu"))));
		colNom.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPaiement().getRdv().getPatient().getMaidenName()));
		colPrenom.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPaiement().getRdv().getPatient().getFirstName()));
		colMontant.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getMontant()));
		colRegler.setCellValueFactory(cellData -> {
			if (cellData.getValue().getPayer() != null)
				return new ReadOnlyStringWrapper(cellData.getValue().getPayer().format(DateTimeFormatter.ofPattern("dd/MM/uuuu")));
			else return new ReadOnlyObjectWrapper<>("");
		});

		tblTp.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				if (newValue.getPayer() != null) {
					newValue.setPayer(null);
				} else {
					newValue.setPayer(LocalDate.now());
				}
				mMedecin.saveTp(newValue);
				tblTp.refresh();
			}
		});
		tblTp.setOnMouseClicked(e -> tblTp.getSelectionModel().clearSelection());
	}

	private void setListiner() {
        mainApp.medecinProperty().addListener((observable, oldValue, newValue) -> {
            cbMedecin.setText(mainApp.getMedecin().toString());
            tblTp.setItems(FXCollections.observableArrayList(mMedecin.getTp(mainApp.medecinProperty().get(), dateProperty.get())));
        });

		dateProperty.addListener((observable, oldValue, newValue) -> {
			lblDate.setText(newValue.format(DateTimeFormatter.ofPattern("MM/uuuu")));
            if (mainApp.medecinProperty().get() != null) {
                tblTp.setItems(FXCollections.observableArrayList(mMedecin.getTp(mainApp.medecinProperty().get(), newValue)));
            }
		});

		btnPrec.setOnAction(e -> dateProperty.set(dateProperty.get().minusMonths(1)));
		btnSuiv.setOnAction(e -> dateProperty.set(dateProperty.get().plusMonths(1)));
	}

    public void updateData() {
        if (mainApp.medecinProperty().isNotNull().get()) {
            cbMedecin.setText(mainApp.getMedecin().toString());
            tblTp.setItems(FXCollections.observableArrayList(mMedecin.getTp(mainApp.medecinProperty().get(), dateProperty.get())));
        }
    }
}
