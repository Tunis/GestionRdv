package app.controller.dialog;

import app.util.AlerteUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import metier.action.MPatient;
import metier.hibernate.data.exceptions.DbCreateException;
import metier.hibernate.data.exceptions.DbDuplicateException;
import models.Adresse;

import java.time.LocalDate;

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
        dialogStage.getScene().setOnKeyReleased(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                handleSubmit();
            }
        });
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
			try {
				numSecu = Integer.valueOf(textFNumSecu.getText());
			} catch (Exception e) {
				
			}
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
			errorMessage += "Prénom non renseigné\n";
		}
		if(nomJf == null || nomJf.isEmpty()){
			errorMessage += "Nom de Jeune Fille  non renseigné\n";
		}
		if(rue == null || rue.isEmpty()){
			errorMessage += "Rue non renseigné\n";
		}
		if(ville == null || ville.isEmpty()){
			errorMessage += "Ville  non renseigné\n";
		}
		if(cp == null || cp.isEmpty()){
			errorMessage += "Code Postal non renseigné\n";
		} else {
			try {
				codePostal = Integer.valueOf(cp);
			} catch (NumberFormatException e) {
				errorMessage += "Code Postal non renseigné\n";
			}
		}
		if(date == null){
			errorMessage += "Date de Naissance non renseigné\n";
		}
		if(tel == null || tel.isEmpty()){
			errorMessage += "Téléphone non renseigné\n";
		}
		if (errorMessage.isEmpty()) {
			//Créer une adresse
			addr = new Adresse(rue, codePostal, ville);
			
			return true;
        } else {
            AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_INCORECT_FIELD, AlerteUtil.HEADERTEXT_INCORECT_FIELD, errorMessage);
            return false;
        }
	}
}
