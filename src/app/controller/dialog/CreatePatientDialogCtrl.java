package app.controller.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import metier.action.MPatient;
import metier.hibernate.data.exceptions.DbCreateException;
import metier.hibernate.data.exceptions.DbDuplicateException;

import java.time.LocalDate;

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


	// a changer
	private MPatient mPatient;
	
	public void setDialogStage(Stage dialogStage, MPatient mPatient) {
        this.mPatient = mPatient;
		this.dialogStage = dialogStage;
    }
	
	@FXML
    private void handleSubmit() {
		//TODO : Enregistrer le Mr dans la BDD !!!

		String adresse = textFAdresse.getText();//*
		// TODO: 25/05/2017 code postal et ville !!!
		String date = textFDate.getText();//*
		String mail = textFMail.getText();
		String nom = textFNom.getText();
		String nomJf = textFNomJF.getText();//*
		String numSecu = textFNumSecu.getText();
		String prenom = textFPrenom.getText();//*
		String tel = textFTel.getText();//*

		if(isValid(adresse, date, nomJf, prenom, tel)){
			try{
				mPatient.createPatient(nom, prenom, nomJf, mail, numSecu, tel, LocalDate.now());
				dialogStage.close();
				// TODO: 25/05/2017 si reussit il faut mettre a jour la liste des patient dans le controller precedent faut l'ajouter pour qu'on puisse l'appeler !
			} catch (DbDuplicateException e) {
				// TODO: 25/05/2017 erreur
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(dialogStage);
				alert.setTitle("Patient deja enregistrer !");
				alert.setHeaderText("les informations semble deja connu");
				alert.setContentText("verifier les champs, ou rechercher le patient.");

				alert.showAndWait();
			}catch (DbCreateException e){
				// TODO: 25/05/2017 erreur
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(dialogStage);
				alert.setTitle("Erreur d'enregistrement dans la bdd !");
				alert.setHeaderText("");
				alert.setContentText("pas trop sur de ce qui a merder :D");

				alert.showAndWait();
			}




//			dialogStage.close();
		}

    }
	
	//Check form, if is valid save data into DB else show pop-up
	private boolean isValid(String adresse, String date, String nomJf, String prenom, String tel){
		String errorMessage = "";
		// TODO: 25/05/2017 changement des type selon le models plzzzz !
		if(prenom == null || prenom.isEmpty()){
			errorMessage += "Champ Prénom invalid\n";
		}
		if(nomJf == null || nomJf.isEmpty()){
			errorMessage += "Champ Nom de Jeune Fille invalid\n";
		}
		if(adresse == null || adresse.isEmpty()){
			errorMessage += "Champ Adresse invalid\n";
		}
		if(date == null || date.isEmpty()){
			errorMessage += "Champ Date de Naissance invalid\n";
		}
		if(tel == null || tel.isEmpty()){
			errorMessage += "Champ Téléphone invalid\n";
		}
		if (errorMessage.isEmpty()) {
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
