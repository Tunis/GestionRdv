package app.controller.dialog;

import app.util.AlerteUtil;
import app.util.RegexUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import metier.action.MMedecin;
import metier.hibernate.data.exceptions.DbSaveException;
import models.Medecin;

public class ProfilMedecinDialogCtrl {
	private Stage dialogStage;
	private MMedecin mMedecin;
	private Medecin medecin;
	
	@FXML
	private TextField textFNom;
	@FXML
	private TextField textFTel;
	@FXML
	private TextField textFPrenom;
	@FXML
	private TextField textFMail;
	
	@FXML
	private Button btnSubmit;
	
	public void setDialogStage(Stage dialogStage, MMedecin mMedecin, Medecin m) {
        this.dialogStage = dialogStage;
        this.mMedecin = mMedecin;
        this.medecin = m;
        
        displayMedecin();
    }
	
	@FXML
    private void handleSubmit() {
		String tel = textFTel.getText();
		String mail = textFMail.getText();
		
		if(isValid(mail, tel)){
			medecin.setEmail(mail);
			medecin.setTelephone(tel);
			
			try {
				mMedecin.save(medecin);
			} catch (DbSaveException e) {
				AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_INCORECT_FIELD, AlerteUtil.HEADERTEXT_INCORECT_FIELD, AlerteUtil.ERROR_MESSAGE_SAVE_DB);
				e.printStackTrace();
			}
			
			dialogStage.close();
		}
    }
	
	private void displayMedecin(){
		textFPrenom.setText(medecin.getFirstName());
		textFNom.setText(medecin.getLastName());
		textFTel.setText(medecin.getTelephone());
		textFMail.setText(medecin.getEmail());
	}
	
	//Check form, if is valid save data into DB else show pop-up
	private boolean isValid(String mail, String tel){
		String errorMessage = "";
		if(!RegexUtil.validateMail(mail)){
			errorMessage += "Mail invalide\n";
		}
		if(tel == null || tel.length() == 0 || !RegexUtil.validateTel(tel)){
			errorMessage += "Tél. invalide\n";
		}
		if (errorMessage.length() == 0) {
            return true;
        } else {
        	AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_INCORECT_FIELD, AlerteUtil.HEADERTEXT_INCORECT_FIELD, errorMessage);
            return false;
        }
	}
}
