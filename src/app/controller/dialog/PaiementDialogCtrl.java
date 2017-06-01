package app.controller.dialog;

import java.time.LocalDate;

import app.util.AlerteUtil;
import app.util.RegexUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import metier.action.MPaiement;
import metier.hibernate.data.exceptions.DbSaveException;
import models.Cheque;
import models.Paiement;
import models.Rdv;
import models.Tp;

public class PaiementDialogCtrl {
	private Stage dialogStage;
	private Rdv rdv;
	private MPaiement mPaiement;
	private Paiement payment;
	
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
	public void setDialogStage(Stage dialogStage, MPaiement mPaiement, Rdv rdv) {
        this.dialogStage = dialogStage;
        this.mPaiement = mPaiement;
        this.rdv = rdv;
        this.payment = rdv.getPaiement();
        
        displayPayment();
    }
	
	private void displayPayment(){
		float cb = 0;
		Cheque cheque = null;
		float espece = 0;
		float prix= 0;
		Tp tp = null;
		boolean payer = false;
		
		try {
			cb = payment.getCb();
			cheque = payment.getCheque();
			espece = payment.getEspece();
			prix = payment.getPrix();
			tp = payment.getTp();
			payer = payment.isPayer();
		} catch (Exception ignore) {}
		
		
		if(cb != 0)
			textFCB.setText(String.valueOf(cb));
		if(espece != 0)
			textFEsp.setText(String.valueOf(espece));
		if(cheque != null){
			textFCheqMontant.setText(String.valueOf(cheque.getMontant()));
			textFCheqBanque.setText(String.valueOf(cheque.getBanque()));
			textFCheqNom.setText(String.valueOf(cheque.getName()));
		}
		if(tp != null)
			textFTP.setText(String.valueOf(tp.getMontant()));
		if(prix != 0)
			textFPrix.setText(String.valueOf(prix));
		if(payer)
			checkBoxPaye.setSelected(true);
	}
	
	@FXML
	public void handleUpadte(){
		//TODO : Vérifié si date du jour => Si oui alors modifier le paiement actuel + cocher "paye"
		//								 => Si Non alors créer un nouveau paiement (avec la date du jour)
		
		LocalDate dateRdv = rdv.getPresentDay().getPresent(); //Date du Rdv
		
		if(isValid()){	
			float cb = Float.valueOf(textFCB.getText());
			
			Cheque cheque = null;
			if(textFCheqMontant.getText() != null || textFCheqMontant.getText().length() != 0){
				cheque = new Cheque();
				cheque.setMontant(Float.valueOf(textFCheqMontant.getText()));
				cheque.setBanque(textFCheqBanque.getText());
				cheque.setName(textFCheqNom.getText());
			}
			
			float espece = Float.valueOf(textFEsp.getText());
			float prix = Float.valueOf(textFPrix.getText());
			float tp = Float.valueOf(textFTP.getText());
			boolean payer = checkBoxPaye.isSelected();

			if(payment != null && dateRdv.equals(LocalDate.now())){
				if(textFCB.getText() != null)
					payment.setCb(cb);
				
				if(textFEsp.getText() != null)
					payment.setEspece(espece);
				
				payment.setCheque(cheque);
				payment.getTp().setMontant(tp); //*
				payment.setPrix(prix); //*
				payment.setPayer(payer);
				
				try {
					mPaiement.editPaiement(payment);
				} catch (DbSaveException e) {
					e.printStackTrace();
					AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_SAVE_DB, AlerteUtil.HEADERTEXT_INCORECT_FIELD, AlerteUtil.ERROR_MESSAGE_SAVE_DB);
				}
			} else {
				Tp newTp = new Tp();
				
				newTp.setMontant(tp);
				
				try {
					mPaiement.createPaiement(espece, cheque, cb, newTp, prix, payer, LocalDate.now(), rdv.getPresentDay().getMedecin(), rdv);
				} catch (DbSaveException e) {
					e.printStackTrace();
					AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_SAVE_DB, AlerteUtil.HEADERTEXT_INCORECT_FIELD, AlerteUtil.ERROR_MESSAGE_SAVE_DB);
				}
			}
			
			dialogStage.close();
		}
	}
	
	//Check form, if is valid save data into DB else show pop-up
	private boolean isValid(){
		String errorMessage = "";
		
		if(!RegexUtil.validateFloatField(textFCB.getText())){
			errorMessage += "Montant Carte bancaire non valide\n";
		}
		if(!RegexUtil.validateFloatField(textFEsp.getText())){
			errorMessage += "Montant Espèce non valide\n";
		}
		if(!RegexUtil.validateFloatField(textFCheqMontant.getText())){
			errorMessage += "Montant Chèque non valide\n";
		}
		if(textFTP.getText() == null || textFTP.getText().length() == 0 || !RegexUtil.validateFloatField(textFTP.getText())){
			errorMessage += "Tiers-Payant non renseigné\n";
		}
		if(textFPrix.getText() == null || textFPrix.getText().length() == 0 || !RegexUtil.validateFloatField(textFPrix.getText())){
			errorMessage += "Prix non renseigné\n";
		}
		if (errorMessage.length() == 0) {
            return true;
        } else {
        	AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_INCORECT_FIELD, AlerteUtil.HEADERTEXT_INCORECT_FIELD, errorMessage);
            return false;
        }
	}
}
