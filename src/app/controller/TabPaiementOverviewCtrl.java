package app.controller;

import app.Main;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import metier.action.MPaiement;
import metier.action.MRdv;
import metier.hibernate.data.exceptions.DbGetException;
import metier.hibernate.data.exceptions.DbSaveException;
import models.Medecin;
import models.Paiement;
import models.Patient;
import models.Rdv;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TabPaiementOverviewCtrl implements Initializable {
	@FXML
	private Label lblDate;

	@FXML
	private ListView<Rdv> listRdvDay;

	@FXML
	private TextField searchField;

	@FXML
	private ListView<Paiement> listPaiement;

	@FXML
	private TableView<Paiement> tblPaiement;

	@FXML
	private TableColumn<Paiement, LocalDateTime> colDate;

	@FXML
	private TableColumn<Paiement, Patient> colPatient;

	@FXML
	private TableColumn<Paiement, Medecin> colMedecin;
	private MRdv mRdv;
	private MPaiement mPaiement;
	private Main mainApp;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// TODO: 03/06/2017 tout plein de out of bound exception ici !!!

		/*
			list rdv du jour :
		 */
		listRdvDay.setCellFactory(new Callback<ListView<Rdv>, ListCell<Rdv>>() {
			@Override
			public ListCell<Rdv> call(ListView<Rdv> param) {
				ListCell<Rdv> cell = new ListCell<Rdv>() {

					@Override
					protected void updateItem(Rdv item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							StringBuilder sb = new StringBuilder();
							LocalTime date = item.getTime();
							sb.append(date.format(DateTimeFormatter.ofPattern("HH:mm"))).append(" : ")
									.append(item.getPatient())
									.append(" avec ").append(item.getPresentDay().getMedecin());
							getStyleClass().add(item.getTypeRdv().name());
							setText(sb.toString());
							if (item.getPaiement() != null && item.getPaiement().getDate() != null) {
								setStyle("-fx-opacity: 0.3");
							}
						} else {
							setText("");
							setStyle("");
							getStyleClass().clear();
						}
					}
				};
				return cell;
			}
		});

		listRdvDay.setOnMouseClicked(event -> {
			listRdvDay.getSelectionModel().clearSelection();
		});

		listRdvDay.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				System.out.println("item : " + newValue);
				mainApp.showEditRdvDialog(newValue, null);
				updateData();
			}
		});

		/*
			search field :
		 */
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.isEmpty()) {
				listPaiement.itemsProperty().unbind();
				List<Paiement> collect = mPaiement.getList().stream().filter(p ->
						p.getRdv().getPatient().getLastName().contains(newValue) ||
								p.getRdv().getPatient().getFirstName().contains(newValue) ||
								p.getRdv().getPatient().getMaidenName().contains(newValue) ||
								p.getMedecin().getFirstName().contains(newValue) ||
								p.getMedecin().getLastName().contains(newValue) ||
								p.getRdv().getPresentDay().getPresent().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")).contains(newValue)
				).collect(Collectors.toList());
				listPaiement.setItems(FXCollections.observableArrayList(collect));
			} else {
				listPaiement.getItems().clear();
				listPaiement.itemsProperty().bind(mPaiement.listProperty());
			}
		});

		/*
			list Paiement Impayer :
		 */

		listPaiement.setCellFactory(new Callback<ListView<Paiement>, ListCell<Paiement>>() {
			@Override
			public ListCell<Paiement> call(ListView<Paiement> param) {
				return new ListCell<Paiement>() {
					@Override
					protected void updateItem(Paiement item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.toString());
						} else {
							setText("");
						}
					}
				};
			}
		});


		listPaiement.setOnMouseClicked(e -> {
			Paiement selectedItem = listPaiement.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				System.out.println("item selectionner : " + selectedItem);
				mainApp.showPaiementDialog(selectedItem.getRdv());
				try {
					mRdv.save(selectedItem.getRdv());
				} catch (DbSaveException ex) {
					ex.printStackTrace();
				}
			}
			listPaiement.getSelectionModel().clearSelection();
			updateData();
		});
	}

	public void initController(Main mainApp, MPaiement mPaiement, MRdv mRdv) {
		this.mainApp = mainApp;
		this.mPaiement = mPaiement;
		this.mRdv = mRdv;
		initData();
	}

	private void initData() {
		listRdvDay.itemsProperty().bind(mRdv.listProperty());
		listPaiement.itemsProperty().bind(mPaiement.listProperty());
	}

	public void updateData() {
		lblDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE dd MMMM u", Locale.getDefault())));
		try {
			listPaiement.itemsProperty().unbind();
			mPaiement.setPaiementImpayer();
			listPaiement.itemsProperty().bind(mPaiement.listProperty());

			listRdvDay.itemsProperty().unbind();
			mRdv.setRdvOfDay(LocalDate.now());
			listRdvDay.itemsProperty().bind(mRdv.listProperty());
		} catch (DbGetException e) {
			e.printStackTrace();
		}
	}
}