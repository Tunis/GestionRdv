package app.controller.dialog;

import app.util.AlerteUtil;
import app.util.RegexUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import models.Cheque;
import models.Paiement;
import models.Rdv;
import models.Tp;

import java.time.LocalDate;

public class PaiementDialogCtrl {
	private Stage dialogStage;
	private Rdv rdv;
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

	public void setDialogStage(Stage dialogStage, Rdv rdv) {
		this.dialogStage = dialogStage;
        this.rdv = rdv;
        this.payment = rdv.getPaiement();

        dialogStage.getScene().setOnKeyReleased(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                handleUpadte();
            }
        });

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
	}
	
	@FXML
	public void handleUpadte(){

		if (isValid()) {
			float cb = 0;
			float espece = 0;
			float prix = 0;
            Cheque cheque;
            Tp tp;
            if (rdv.getPaiement() != null) {
                tp = rdv.getPaiement().getTp() != null ? rdv.getPaiement().getTp() : null;
                cheque = rdv.getPaiement().getCheque() != null ? rdv.getPaiement().getCheque() : null;
            } else {
                tp = null;
                cheque = null;
            }
            if (!textFCB.getText().isEmpty())
				cb = Float.valueOf(textFCB.getText());
			if (!textFEsp.getText().isEmpty())
				espece = Float.valueOf(textFEsp.getText());
			if (!textFPrix.getText().isEmpty())
				prix = Float.valueOf(textFPrix.getText());

			if (!textFTP.getText().isEmpty()) {
                if (tp == null)
                    tp = new Tp();
                tp.setMontant(Float.parseFloat(textFTP.getText()));
			}

			if (!textFCheqMontant.getText().isEmpty()) {
                if (cheque == null)
                    cheque = new Cheque();
                cheque.setMontant(Float.valueOf(textFCheqMontant.getText()));
				cheque.setBanque(textFCheqBanque.getText());
				cheque.setName(textFCheqNom.getText());
			}


			if (payment != null) {
				// modification du paiement :
				setPayment(espece, cb, prix, cheque, tp);
			}else{
				// nouveau paiement :
				payment = new Paiement();
				setPayment(espece, cb, prix, cheque, tp);
				rdv.setPaiement(payment);
				payment.setRdv(rdv); // nouveau paiement on le lie au rdv.
				payment.setMedecin(rdv.getPresentDay().getMedecin());
			}
			// plus de save en bdd ici c'est le rdv qui gere tout seul au retour de la fenetre ca evite des probleme si on ferme la fenetre du rdv sans le save.
			dialogStage.close();
		}
	}
	
	//Check form, if is valid save data into DB else show pop-up
	private boolean isValid(){
		String errorMessage = "";
		// les champs ont le droit d'etre vide mais doivent etre des float :

		if (!textFCB.getText().isEmpty()) {
			if(!RegexUtil.validateFloatField(textFCB.getText())){
				errorMessage += "Montant Carte bancaire non valide\n";
			}
		}
		if (!textFEsp.getText().isEmpty()) {
			if(!RegexUtil.validateFloatField(textFEsp.getText())){
				errorMessage += "Montant Espèce non valide\n";
			}
		}
		if (!textFCheqMontant.getText().isEmpty()) {
			if (!RegexUtil.validateFloatField(textFCheqMontant.getText()))
				errorMessage += "Montant Chèque non valide\n";
			if (textFCheqNom.getText().isEmpty())
				errorMessage += "nom du Chèque non valide\n";
			if (textFCheqBanque.getText().isEmpty())
				errorMessage += "banque du Chèque non valide\n";
		}
		if (!textFTP.getText().isEmpty()) {
			if (!RegexUtil.validateFloatField(textFTP.getText())) {
				errorMessage += "Tiers-Payant non valide\n";
			}
		}

		// le prix est obligatoire
		if (textFPrix.getText().isEmpty()) {
			if (!RegexUtil.validateFloatField(textFPrix.getText())) {
				errorMessage += "Prix non valide\n";
			} else
				errorMessage += "Prix non renseigné\n";
		}
		if (errorMessage.length() == 0) {
            return true;
        } else {
        	AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_INCORECT_FIELD, AlerteUtil.HEADERTEXT_INCORECT_FIELD, errorMessage);
            return false;
        }
	}

	private void setPayment(float espece, float cb, float prix, Cheque cheque, Tp tp) {
		payment.setCb(cb);
		payment.setEspece(espece);
		payment.setCheque(cheque);
		payment.setTp(tp); //*
		payment.setPrix(prix); //*
        if (tp != null)
            tp.setPaiement(payment);
		if (payment.isPayer())
			payment.setDate(LocalDate.now());
	}
}
