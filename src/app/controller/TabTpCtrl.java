package app.controller;

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
	private ComboBox<Medecin> cbMedecin;
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
	private ObjectProperty<Medecin> medecin = new SimpleObjectProperty<>();

	public void initController(MMedecin mMedecin) {
		this.mMedecin = mMedecin;
		cbMedecin.itemsProperty().bind(mMedecin.listProperty());

		setListiner();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cbMedecin.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
			medecin.set(cbMedecin.getItems().get((Integer) newValue));
		});
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
		medecin.addListener((observable, oldValue, newValue) -> {
			tblTp.setItems(FXCollections.observableArrayList(mMedecin.getTp(medecin.get(), dateProperty.get())));
		});

		dateProperty.addListener((observable, oldValue, newValue) -> {
			lblDate.setText(newValue.format(DateTimeFormatter.ofPattern("MM/uuuu")));
			if (medecin.get() != null) {
				tblTp.setItems(FXCollections.observableArrayList(mMedecin.getTp(getSelectedMedecin(), newValue)));
			}
		});

		btnPrec.setOnAction(e -> dateProperty.set(dateProperty.get().minusMonths(1)));
		btnSuiv.setOnAction(e -> dateProperty.set(dateProperty.get().plusMonths(1)));
	}

	private Medecin getSelectedMedecin() {
		return cbMedecin.getItems().get(cbMedecin.getSelectionModel().getSelectedIndex());
	}
}
