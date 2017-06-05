package app.controller.dialog;


import app.Main;
import app.util.AlerteUtil;
import app.util.RegexUtil;
import com.google.common.eventbus.Subscribe;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import metier.action.MPatient;
import metier.hibernate.data.exceptions.DbGetException;
import metier.hibernate.data.exceptions.DbSaveException;
import models.Medecin;
import models.Patient;
import models.Rdv;
import models.enums.TypeRdv;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ProfilPatientDialogCtrl {
	private Main mainApp;
	private Stage dialogStage;
	private MPatient mPatient;
	private Patient p;
	
	@FXML
	private Button btnSubmit;
	
	@FXML
	private DatePicker dpDate;
	
	@FXML
	private TextField textFPrenom;
	@FXML
	private TextField textFNom;
	@FXML
	private TextField textFNomJF;
	@FXML
	private TextField textFAdresse;
	@FXML
	private TextField textFVille;
	@FXML
	private TextField textFCP;
	@FXML
	private TextField textFMail;
	@FXML
	private TextField textFTel;
	@FXML
	private TextField textFNSecu;
	@FXML
	private TextField textFNote;
	
	@FXML
	private TableView<Rdv> tableRdv;
	@FXML
	private TableColumn<Rdv, LocalDate> colDate;
	@FXML
	private TableColumn<Rdv, TypeRdv> colCat;
	@FXML
	private TableColumn<Rdv, Medecin> ColMed;
	@FXML
	private TableColumn<Rdv, String> colPay;

	public TableView<Rdv> geTableView(){
		return tableRdv;
	}
	
	public void setDialogStage(Stage dialogStage, Main mainApp, MPatient mPatient, Patient p) {
		this.mainApp = mainApp;
		this.dialogStage = dialogStage;
		this.mPatient = mPatient;
		this.p = p;
		
		//Affiche le profil
		displayProfil();
		
		//Affiche la liste des Rdv
		listRdv();
	}
	
	private void displayProfil(){
		textFPrenom.setText(p.getFirstName());
		textFNom.setText(p.getLastName());
		textFNomJF.setText(p.getMaidenName());
		dpDate.setValue(p.getBornDate());
		textFAdresse.setText(p.getAdresse().getRue());
		textFCP.setText(String.valueOf(p.getAdresse().getCodePostal()));;
		textFVille.setText(p.getAdresse().getVille());
		textFMail.setText(p.getEmail());
		textFTel.setText(p.getTelephone());
		textFNSecu.setText(String.valueOf(p.getSecuNumber()));
		textFNote.setText(p.getNote());
	}
	
	//Check form, if is valid save data into DB else show pop-up
	private boolean isValid(String adresse, String ville, String cp, String mail, String tel, String nSecu) {
		String errorMessage = "";

		if (!adresse.isEmpty() || !ville.isEmpty() || !cp.isEmpty()) {
			if (adresse.isEmpty())
				errorMessage += "Rue non renseigné\n";
			if (ville.isEmpty())
				errorMessage += "Ville  non renseigné\n";
			if (cp.isEmpty())
				errorMessage += "Code Postal non renseigné\n";
			else {
				try {
					int codePostal = Integer.valueOf(cp);
				} catch (NumberFormatException e) {
					errorMessage += "Code Postal non renseigné\n";
				}
			}
		}

		if (!mail.isEmpty() && !RegexUtil.validateMail(mail)) {
			errorMessage += "Mail non valide\n";
		}
		if (tel.isEmpty() || !RegexUtil.validateTel(tel)) {
			errorMessage += "Tel. non valide\n";
		}
		if (!nSecu.isEmpty() && !RegexUtil.validateIntField(nSecu)) {
			errorMessage += "Numéro Sécurité non valide\n";
		}
		if (errorMessage.length() == 0) {
            return true;
        } else {
            AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_INCORECT_FIELD, AlerteUtil.HEADERTEXT_INCORECT_FIELD, errorMessage);

            return false;
        }
	}
	
	private void listRdv(){
		tableRdv.itemsProperty().unbind();
		tableRdv.itemsProperty().bind(new SimpleListProperty<>(FXCollections.observableArrayList(p.getRdvList())));

		colDate.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPresentDay().getPresent()));
		colCat.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getTypeRdv()));
		ColMed.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPresentDay().getMedecin()));
		colPay.setCellValueFactory(cellData -> {
			if (cellData.getValue().getPaiement() != null && cellData.getValue().getPaiement().getDate() != null) {
				return new ReadOnlyObjectWrapper<>(cellData.getValue().getPaiement().getDate().format(DateTimeFormatter.ofPattern("dd/MM/uuuu", Locale.getDefault()))).asString();
			} else {
				return new ReadOnlyObjectWrapper<>("").asString();
			}
		});

		tableRdv.getSortOrder().clear();
		tableRdv.getSortOrder().add(colDate);
		colDate.setSortType(TableColumn.SortType.DESCENDING);
		tableRdv.sort();
		
		//Permet d'éditer les rdv
		tableRdv.setOnMouseClicked(event -> {
			if (!tableRdv.getSelectionModel().isEmpty() && tableRdv.getSelectionModel().getSelectedIndex() != -1) {
				Rdv selectedRdv = tableRdv.getItems().get(tableRdv.getSelectionModel().getSelectedIndex());


				if (selectedRdv != null) {
					mainApp.showEditRdvDialog(selectedRdv, this);
					updatePatient();
				}
			}
		});
	}
	
	@FXML
    private void handleSubmit() {
		
		String cp = textFCP.getText();
		String nSecu = textFNSecu.getText();
		String adresse = textFAdresse.getText();
		String ville = textFVille.getText();
		String tel = textFTel.getText();
		String mail = textFMail.getText();
		int secu = 0;

		if (isValid(adresse, ville, cp, mail, tel, nSecu)) {
			if (!nSecu.isEmpty())
				secu = Integer.valueOf(nSecu);
			p.getAdresse().setRue(textFAdresse.getText());
			p.getAdresse().setCodePostal(Integer.valueOf(textFCP.getText()));
			p.getAdresse().setVille(textFVille.getText());
			p.setEmail(textFMail.getText());
			p.setLastName(textFNom.getText());
			p.setNote(textFNote.getText());
			p.setSecuNumber(secu);
			p.setTelephone(textFTel.getText());
			
			try {
				mPatient.save(p);
				mPatient.updateList();
			} catch (DbSaveException e) {
				e.printStackTrace();
				AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_INCORECT_FIELD, AlerteUtil.HEADERTEXT_CREATE_DB, AlerteUtil.ERROR_MESSAGE_SAVE_DB);
			} catch (DbGetException e) {
				e.printStackTrace();
				AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_SAVE_DB, AlerteUtil.HEADERTEXT_CREATE_DB, AlerteUtil.ERROR_MESSAGE_SAVE_DB);
			}

			dialogStage.close();
		}
    }

	@Subscribe
	private void updatePatient() {
		displayProfil();
		tableRdv.refresh();
	}
}
