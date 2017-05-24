package app.controller.dialog;

import javafx.stage.Stage;

public class ProfilMedecinDialogCtrl {
	private Stage dialogStage;
	
	/*@FXML
	private Button btnSubmit;*/
	
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
	/*@FXML
    private void handleSubmit() {
		//TODO : Enregistrer le Mr dans la BDD !!!
		
		
			textFAdresse.getText(); *
			textFDate.getText(); *
			textFMail.getText();
			textFNom.getText();
			textFNomJF.getText(); *
			textFNumSecu.getText();
			textFPrenom.getText(); *
			textFTel.getText(); *
		
		
		if(isInvalid()){
			dialogStage.close();
		}
    }*/
	
	//Check form, if is valid save data into DB else show pop-up
	/*private boolean isInvalid(){
		String errorMessage = "";
		
		if(textFPrenom.getText() == null || textFPrenom.getText().length() == 0){
			errorMessage += "Champ Pr�nom invalid\n";
		
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
	}*/
}
