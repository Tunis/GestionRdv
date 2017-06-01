package app.controller.dialog;


import java.time.LocalDate;


import app.Main;
import app.util.AlerteUtil;
import app.util.RegexUtil;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import metier.action.MPatient;
import metier.hibernate.data.exceptions.DbSaveException;
import models.Medecin;
import models.Patient;
import models.Rdv;
import models.TypeRdv;

public class ProfilPatientDialogCtrl {
	private Main mainApp;
	private Stage dialogStage;
	private MPatient mPatient;
	private Patient p;
	
	@FXML
	private Button btnSubmit;
	
	@FXML
	private DatePicker dpDate;
	
	@FXML
	private TextField textFPrenom;
	@FXML
	private TextField textFNom;
	@FXML
	private TextField textFNomJF;
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
	
	@FXML
	private TableView<Rdv> tableRdv;
	@FXML
	private TableColumn<Rdv, LocalDate> colDate;
	@FXML
	private TableColumn<Rdv, TypeRdv> colCat;
	@FXML
	private TableColumn<Rdv, Medecin> ColMed;
	@FXML
	private TableColumn<Rdv, Boolean> colPay;

	public TableView<Rdv> geTableView(){
		return tableRdv;
	}
	
	public void setDialogStage(Stage dialogStage, Main mainApp, MPatient mPatient, Patient p) {
		this.mainApp = mainApp;
		this.dialogStage = dialogStage;
		this.mPatient = mPatient;
		this.p = p;
		
		//Affiche le profil
		displayProfil();
		
		//Affiche la liste des Rdv
		listRdv();
	}
	
	private void displayProfil(){
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
	
	//Check form, if is valid save data into DB else show pop-up
	private boolean isValid(String cp, String nSecu){
		String errorMessage = "";
		
		if(textFPrenom.getText() == null || textFPrenom.getText().length() == 0){
			errorMessage += "Prénom non renseigné\n";
		}
		if(textFNom.getText() == null || textFNom.getText().length() == 0){
			errorMessage += "Nom non renseigné\n";
		}
		if(textFNomJF.getText() == null || textFNomJF.getText().length() == 0){
			errorMessage += "Nom de Jeune Fille non renseigné\n";
		}
		if(dpDate.getEditor().getText() == null || dpDate.getEditor().getText().length() == 0){
			errorMessage += "Date de naissance non renseigné\n";
		}
		if(textFAdresse.getText() == null || textFAdresse.getText().length() == 0){
			errorMessage += "Rue non renseigné\n";
		}
		if(textFVille.getText() == null || textFVille.getText().length() == 0){
			errorMessage += "Ville non renseigné\n";
		}
		if(!RegexUtil.validateIntField(cp)){
			errorMessage += "Code Postal non renseigné\n";
		}
		if(!RegexUtil.validateMail(textFMail.getText())){
			errorMessage += "Mail non valide\n";
		}
		if(!RegexUtil.validateTel(textFTel.getText())){
			errorMessage += "Tel. non valide\n";
		}
		if(!RegexUtil.validateIntField(textFNSecu.getText())){
			errorMessage += "Numéro Sécurité non valide\n";
		}
		if (errorMessage.length() == 0) {
            return true;
        } else {
            AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_INCORECT_FIELD, AlerteUtil.HEADERTEXT_INCORECT_FIELD, errorMessage);

            return false;
        }
	}
	
	private void listRdv(){
		tableRdv.itemsProperty().bind(new SimpleListProperty<>(FXCollections.observableArrayList(p.getRdvList())));
		
		colDate.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPresentDay().getPresent()));
		colCat.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getTypeRdv()));
		ColMed.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPresentDay().getMedecin()));
		colPay.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPaiement() == null ? 
				false : 	
				cellData.getValue().getPaiement().isPayer()));
		
		//Permet d'éditer les rdv
		tableRdv.setOnMouseClicked(event -> {
			Rdv selectedRdv = tableRdv.getSelectionModel().getSelectedItem();
	        
	        if(selectedRdv != null){
	        	mainApp.showEditRdvDialog(selectedRdv, this);
	        }
		});
	}
	
	@FXML
    private void handleSubmit() {
		
		String cp = textFCP.getText();
		String nSecu = textFNSecu.getText();
		
		if(isValid(cp, nSecu)){
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
				AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_INCORECT_FIELD, AlerteUtil.HEADERTEXT_CREATE_DB, AlerteUtil.ERROR_MESSAGE_SAVE_DB);
			}
			
			dialogStage.close();
		}
    }
}
