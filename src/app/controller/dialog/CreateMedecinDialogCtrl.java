package app.controller.dialog;


import app.util.AlerteUtil;
import app.util.RegexUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import metier.action.MMedecin;
import metier.hibernate.data.exceptions.DbCreateException;
import metier.hibernate.data.exceptions.DbDuplicateException;

public class CreateMedecinDialogCtrl {
	private Stage dialogStage;
	private MMedecin mMedecin;
	
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
	
	public void setDialogStage(Stage dialogStage, MMedecin mMedecin) {
        this.dialogStage = dialogStage;
        this.mMedecin = mMedecin;
    }
	
	@FXML
    private void handleSubmit() {
		String firstName = textFPrenom.getText();
		String lastName = textFNom.getText();
		String tel = textFTel.getText();
		String mail = textFMail.getText(); 
		
		if(isValid(tel, mail)){
			try {
				mMedecin.createMedecin(lastName, firstName, mail, tel);
				dialogStage.close();
			} catch (DbDuplicateException e1) {
				e1.printStackTrace();
				AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_CREATE_MEDECIN_DB, AlerteUtil.HEADERTEXT_CREATE_DB, AlerteUtil.ERROR_MESSAGE_CREATE_MEDECIN_DB);
			} catch (DbCreateException e1) {
				e1.printStackTrace();
				AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_CREATE_MEDECIN_DB, AlerteUtil.HEADERTEXT_CREATE_DB, AlerteUtil.ERROR_MESSAGE_SAVE_DB);
			}
		}
    }
	
	//Check form, if is valid save data into DB else show pop-up
	private boolean isValid(String tel, String mail){
		String errorMessage = "";
		
		if(!RegexUtil.validateTel(tel)){
			System.out.println("tel : " + tel);
			errorMessage += "Tél. invalide\n";
		}
		if(!RegexUtil.validateMail(mail)){
			System.out.println("mail : " + mail);
			errorMessage += "Mail invalide\n";
		}
		if(textFPrenom.getText() == null || textFPrenom.getText().length() == 0){
			errorMessage += "Prénom non renseigné\n";
		}
		
		if(textFNom.getText() == null || textFNom.getText().length() == 0){
			errorMessage += "Nom  non renseigné\n";
		}
		
		if (errorMessage.length() == 0) {
            return true;
        } else {
        	AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_INCORECT_FIELD, AlerteUtil.HEADERTEXT_INCORECT_FIELD, errorMessage);
            return false;
        }
	}
}
