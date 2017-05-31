package app.controller.dialog;

import java.time.Duration;
import java.time.LocalTime;

import app.util.AlerteUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import metier.action.MMedecin;
import models.Medecin;
import models.Paiement;
import models.Rdv;
import models.TypeRdv;

public class EditRdvDialogCtrl {
	private Stage dialogStage;
	private MMedecin mMedecin;
	private Rdv rdv;
	private Paiement payment;
	
	@FXML
    private DatePicker dpDate;

	@FXML
    private Label labNamePatient;
	
    @FXML
    private ComboBox<Medecin> cbMedecin;
    @FXML
    private ComboBox<TypeRdv> cbType;
    
    @FXML
    private Spinner<Integer> spHeure;
    @FXML
    private Spinner<Integer> spMinute;
    @FXML
    private Spinner<Integer> spDuree;
    
    @FXML
    private TextField textFCotation;
    
    @FXML
    private Button btnSubmit;
    @FXML
    private Button btnPayment;
    
	public void setDialogStage(Stage dialogStage, Rdv rdv, MMedecin mMedecin) {
		this.mMedecin = mMedecin;
		this.dialogStage = dialogStage;
		this.rdv = rdv;
		
		displayRdv();
    }
	
	//Affiche le RdV
	private void displayRdv(){
		
		dpDate.setValue(rdv.getPresentDay().getPresent());
		labNamePatient.setText(rdv.getPatient().getMaidenName());
		
		cbMedecin.itemsProperty().bind(mMedecin.listProperty());
		cbMedecin.setValue(rdv.getPresentDay().getMedecin());
		
		spDuree.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(15,120,30,15));
		spDuree.getValueFactory().setValue(rdv.getDuration().getNano());
		
        spHeure.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23,8,1));
        spHeure.getValueFactory().setValue(rdv.getTime().getHour());
        
        
        spMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,0,15));
        spMinute.getValueFactory().setValue(rdv.getTime().getMinute());
        
		cbType.setItems(FXCollections.observableArrayList(TypeRdv.values()));
		cbType.setValue(rdv.getTypeRdv());
		
		textFCotation.setText(rdv.getCotation());
	}
	
	public void handleUpadte(){
		
		if(isValid()){
			//update Object Rdv
			rdv.getPresentDay().setPresent(dpDate.getValue());
			rdv.setDuration(Duration.ofMinutes(spDuree.getValue()));
			rdv.setCotation(textFCotation.getText());
			rdv.setTime(LocalTime.of(Integer.valueOf(spHeure.getEditor().getText()), Integer.valueOf(spMinute.getEditor().getText())));
			rdv.setTypeRdv(cbType.getValue());
			//rdv.setPaiement(paiement);
			
			//TODO : Faire la save dans la Base
			/*try {
				MRdv.save(rdv);
			} catch (DbSaveException e) {
				e.printStackTrace();
			}*/
			
			dialogStage.close();
		}
	}
	
	public void handlePayment(){
		//TODO : Affiche le pop-up pour renseigner le paiement
		//Besoin d'un retour paiement
		//payment = ??;
	}
	
	//Check form, if is valid save data into DB else show pop-up
	private boolean isValid(){
		String errorMessage = "";
		
		int heure = 0;
		int minute = 0;
		
		try {
			heure = Integer.valueOf(spHeure.getEditor().getText());
			minute = Integer.valueOf(spMinute.getEditor().getText());
			
			if(spHeure.getEditor().getText() == null || heure >= 24){
				errorMessage += "Heure du Rdv invalide\n";
			}
			if(spMinute.getEditor().getText() == null || minute >= 60){
				errorMessage += "Minute du Rdv invalide\n";
			}
		} catch (NumberFormatException e) {
			errorMessage += "Horaire invalide\n";
		}
		if(dpDate.getEditor().getText() == null || dpDate.getEditor().getText().length() == 0){
			errorMessage += "Date RdV invalide\n";
		}
		if(payment == null){
			errorMessage += "Paiement non renseign�\n";
		}
		if (errorMessage.length() == 0) {
            return true;
        } else {
            AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_INCORECT_FIELD, AlerteUtil.HEADERTEXT_INCORECT_FIELD, errorMessage);

            return false;
        }
	}
}
