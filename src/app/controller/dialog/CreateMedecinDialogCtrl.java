package app.controller.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateMedecinDialogCtrl {
	private Stage dialogStage;
	
	@FXML
	private TextField textFNom;
	@FXML
	private TextField textFMail;
	@FXML
	private TextField textFTel;
	@FXML
	private TextField textFPrenom;
	
	@FXML
	private Button btnSubmit;
	
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
	@FXML
    private void handleSubmit() {
		//TODO : Enregistrer le Mr dans la BDD !!!
		/*
			textFNom.getText(); *
			textFMail.getText(); 
			textFTel.getText();
			textFPrenom.getText(); *
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
		
		if(textFNom.getText() == null || textFNom.getText().length() == 0){
			errorMessage += "Champ Nom invalid\n";
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
