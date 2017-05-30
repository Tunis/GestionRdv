package app.controller.dialog;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import metier.action.MPatient;
import metier.hibernate.data.exceptions.DbSaveException;
import models.Patient;

public class ProfilPatientDialogCtrl {
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
	
	public TextField getTextFPrenom() {return textFPrenom;}

	public TextField getTextFNom() {return textFNom;}
	
	public TextField getTextFNomJF() {return textFNomJF;}

	public DatePicker getDpDate() {return dpDate;}

	public TextField getTextFAdresse() {return textFAdresse;}

	public TextField getTextFMail() {return textFMail;}

	public TextField getTextFTel() {return textFTel;}

	public TextField getTextFNSecu() {return textFNSecu;}

	public TextField getTextFNote() {return textFNote;}

	public void setDialogStage(Stage dialogStage, MPatient mPatient, Patient p) {
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
		
		if(isInvalid()){
			dialogStage.close();
		}
    }
	
	//Check form, if is valid save data into DB else show pop-up
	private boolean isInvalid(){
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
}
