package app.controller.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import metier.action.MPatient;
import metier.hibernate.data.exceptions.DbCreateException;
import metier.hibernate.data.exceptions.DbDuplicateException;
import models.Adresse;

import java.time.LocalDate;

import app.util.AlerteUtil;

public class CreatePatientDialogCtrl {
	
	private Stage dialogStage;
	private Adresse addr;
	
	@FXML
	private TextField textFNom;
	@FXML
	private TextField textFNomJF;
	@FXML
	private TextField textFPrenom;
	@FXML
	private TextField textFAdresse;
	@FXML
	private TextField textFVille;
	@FXML
	private TextField textFCP;
	@FXML
	private TextField textFTel;
	@FXML
	private TextField textFMail;
	@FXML
	private DatePicker dpDate;
	@FXML
	private TextField textFNumSecu;
    
	@FXML
	private Button btnSubmit;

	//TODO : a changer
	private MPatient mPatient;
	
	public void setDialogStage(Stage dialogStage, MPatient mPatient) {
        this.mPatient = mPatient;
		this.dialogStage = dialogStage;
    }
	
	@FXML
    private void handleSubmit() {
		int numSecu = 0;
		
		String rue = textFAdresse.getText();//*
		String ville = textFVille.getText();
		String cp = textFCP.getText();
		LocalDate date = dpDate.getValue();//*
		String mail = textFMail.getText();
		String nom = textFNom.getText();
		String nomJf = textFNomJF.getText();//*
		if(textFNumSecu.getText() != null){
			numSecu = Integer.valueOf(textFNumSecu.getText());
		}
		String prenom = textFPrenom.getText();//*
		String tel = textFTel.getText();//*

		if(isValid(rue, ville, cp, date, nomJf, prenom, tel)){
			try{
				mPatient.createPatient(nom, prenom, nomJf, mail, numSecu, tel, date, addr);
				dialogStage.close();
				// TODO: 25/05/2017 si reussit il faut mettre a jour la liste des patient dans le controller precedent faut l'ajouter pour qu'on puisse l'appeler !
			} catch (DbDuplicateException e) {
				AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_CREATE_PATIENT_DB, AlerteUtil.HEADERTEXT_CREATE_DB, AlerteUtil.ERROR_MESSAGE_CREATE_PATIENT_DB);
			} catch (DbCreateException e){
				AlerteUtil.showAlerte(dialogStage, AlerteUtil.HEADERTEXT_CREATE_DB, AlerteUtil.HEADERTEXT_CREATE_DB, AlerteUtil.ERROR_MESSAGE_SAVE_DB);
			}
		}

    }
	
	//Check form, if is valid save data into DB else show pop-up
	private boolean isValid(String rue, String ville, String cp, LocalDate date, String nomJf, String prenom, String tel){
		String errorMessage = "";
		int codePostal = 0;
		// TODO: 25/05/2017 changement des type selon le models plzzzz !
		if(prenom == null || prenom.isEmpty()){
			errorMessage += "Champ Prénom invalid\n";
		}
		if(nomJf == null || nomJf.isEmpty()){
			errorMessage += "Champ Nom de Jeune Fille invalid\n";
		}
		if(rue == null || rue.isEmpty()){
			errorMessage += "Champ Adresse invalid\n";
		}
		if(ville == null || ville.isEmpty()){
			errorMessage += "Champ Ville invalid\n";
		}
		if(cp == null || cp.isEmpty()){
			errorMessage += "Champ CP invalid\n";
		} else {
			try {
				codePostal = Integer.valueOf(cp);
			} catch (NumberFormatException e) {
				errorMessage += "Champ CP invalid\n";
			}
		}
		if(date == null){
			errorMessage += "Champ Date de Naissance invalid\n";
		}
		if(tel == null || tel.isEmpty()){
			errorMessage += "Champ Téléphone invalid\n";
		}
		if (errorMessage.isEmpty()) {
			//Cr�er une adresse
			addr = new Adresse(rue, codePostal, ville);
			
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
}
