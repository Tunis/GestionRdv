package app.controller.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreatePatientDialogCtrl {
	
	private Stage dialogStage;
	
	@FXML
	private TextField textFNom;
	@FXML
	private TextField textFNomJF;
	@FXML
	private TextField textFPrenom;
	@FXML
	private TextField textFAdresse;
	@FXML
	private TextField textFTel;
	@FXML
	private TextField textFMail;
	@FXML
	private TextField textFDate;
	@FXML
	private TextField textFNumSecu;
    
	@FXML
	private Button btnSubmit;
	
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
	@FXML
    private void handleSubmit() {
		//TODO : Enregistrer le Mr dans la BDD !!!
		
		/*
			textFAdresse.getText(); *
			textFDate.getText(); *
			textFMail.getText();
			textFNom.getText();
			textFNomJF.getText(); *
			textFNumSecu.getText();
			textFPrenom.getText(); *
			textFTel.getText(); *
		*/
		
		if(isInvalid()){
			dialogStage.close();
		}
    }
	
	//Check form, if is valid save data into DB else show pop-up
	private boolean isInvalid(){
		String errorMessage = "";
		
		if(textFPrenom.getText() == null || textFPrenom.getText().length() == 0){
			errorMessage += "Champ Pr�nom invalid\n";
		}
		
		if(textFNomJF.getText() == null || textFNomJF.getText().length() == 0){
			errorMessage += "Champ Nom de Jeune Fille invalid\n";
		}
		
		if(textFAdresse.getText() == null || textFAdresse.getText().length() == 0){
			errorMessage += "Champ Adresse invalid\n";
		}
		
		if(textFDate.getText() == null || textFDate.getText().length() == 0){
			errorMessage += "Champ Date de Naissance invalid\n";
		}
		
		if(textFTel.getText() == null || textFTel.getText().length() == 0){
			errorMessage += "Champ T�l�phone invalid\n";
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
