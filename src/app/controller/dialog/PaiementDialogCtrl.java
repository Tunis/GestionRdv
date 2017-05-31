package app.controller.dialog;

import java.time.Duration;
import java.time.LocalTime;

import app.util.AlerteUtil;
import app.util.RegexUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import metier.action.MPaiement;
import models.Paiement;

public class PaiementDialogCtrl {
	private Stage dialogStage;
	private Paiement paiement;
	private MPaiement mPaiement;
	
	@FXML
	private TextField textFCB;
	@FXML
	private TextField textFEsp;
	@FXML
	private TextField textFTP;
	@FXML
	private TextField textFPrix;
	@FXML
	private TextField textFCheqMontant;
	@FXML
	private TextField textFCheqBanque;
	@FXML
	private TextField textFCheqNom;
	
	@FXML
	private CheckBox checkBoxPaye;
	
	@FXML
	private Button btnSubmit;
       
	/*public void setDialogStage(Stage dialogStage, MPaiement mPaiement, Paiement paiement) {
        this.dialogStage = dialogStage;
        this.mPaiement = mPaiement;
        this.paiement = paiement;
    }*/
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
	@FXML
	public void handleUpadte(){
		//TODO : Vérifié si date du jour => Si oui alors modifier le paiement actuel + cocher "paye"
		//								 => Si Non alors créer un nouveau paiement
		
		/*
		if(isValid()){
			
			paiement.setCb(cb);
			paiement.getCheque().setBanque(banque);
			paiement.getCheque().setMontant(montant));
			paiement.getCheque().setName(name);;
			paiement.setEspece(espece);
			paiement.setTp(tp);
			paiement.setPrix(prix);
			paiement.setPayer(payer);
			*/
			//TODO : Faire la save dans la Base
			/*try {
				MRdv.save(rdv);
			} catch (DbSaveException e) {
				e.printStackTrace();
			}*/
			
			dialogStage.close();
	}
	
	//Check form, if is valid save data into DB else show pop-up
	/*private boolean isValid(String mail, String tel){
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
	}*/
}
