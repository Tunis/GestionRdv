package app.controller.dialog;


import java.time.LocalDate;

import app.Main;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import metier.action.MPatient;
import metier.hibernate.data.exceptions.DbSaveException;
import models.Medecin;
import models.Patient;
import models.Rdv;
import models.TypeRdv;

public class ProfilPatientDialogCtrl {
	private Main mainApp;
	private Stage dialogStage;
	private MPatient mPatient;
	private Patient p;
	
	@FXML
	private Button btnSubmit;
	
	@FXML
	private TextField textFPrenom;
	@FXML
	private TextField textFNom;
	@FXML
	private TextField textFNomJF;
	@FXML
	private DatePicker dpDate;
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
	private TableColumn<Rdv, Boolean> colPay;

	public void setDialogStage(Stage dialogStage, Main mainApp, MPatient mPatient, Patient p) {
		this.mainApp = mainApp;
		this.dialogStage = dialogStage;
		this.mPatient = mPatient;
		this.p = p;
		
		//Affiche le profil
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
		
		//Affiche la liste des Rdv
		listRdv();
	}
	
	@FXML
    private void handleSubmit() {
		p.getAdresse().setRue(textFAdresse.getText());
		p.getAdresse().setCodePostal(Integer.valueOf(textFCP.getText()));
		p.getAdresse().setVille(textFVille.getText());
		p.setEmail(textFMail.getText());
		p.setLastName(textFNom.getText());
		p.setNote(textFNote.getText());
		p.setSecuNumber(Integer.valueOf(textFNSecu.getText()));
		p.setTelephone(textFTel.getText());
		
		try {
			mPatient.save(p);
		} catch (DbSaveException e) {
			e.printStackTrace();
		}
		
		if(isValid()){
			dialogStage.close();
		}
    }
	
	//Check form, if is valid save data into DB else show pop-up
	private boolean isValid(){
		String errorMessage = "";
		
		if(textFPrenom.getText() == null || textFPrenom.getText().length() == 0){
			errorMessage += "Champ Prénom invalid\n";
		}
		
		if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Erreur de Renseignement");
            alert.setHeaderText("Merci de modifier les champ incorrect");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
	}
	
	private void listRdv(){
		
		tableRdv.itemsProperty().bind(new SimpleListProperty<>(FXCollections.observableArrayList(p.getRdvList())));
		
		colDate.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPresentDay().getPresent()));
		colCat.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getTypeRdv()));
		ColMed.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPresentDay().getMedecin()));
		colPay.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPaiement() == null ? 
				false : 	
				cellData.getValue().getPaiement().isPayer()));
		
		tableRdv.setOnMouseClicked(event -> {
			Rdv selectedRdv = tableRdv.getSelectionModel().getSelectedItem();
	        
	        if(selectedRdv != null){
	        	mainApp.showEditRdvDialog(selectedRdv);
	        }
		});
	}
}
