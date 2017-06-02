package app.controller.dialog;

import java.time.Duration;
import java.time.LocalTime;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import metier.action.MMedecin;
import models.Medecin;
import models.Paiement;
import models.Rdv;
import models.enums.TypeRdv;

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
		//update Object Rdv
		rdv.getPresentDay().setPresent(dpDate.getValue());
		rdv.setDuration(Duration.ofMinutes(spDuree.getValue()));
		rdv.setCotation(textFCotation.getText());
		rdv.setTime(LocalTime.of(spHeure.getValue(), spMinute.getValue()));
		rdv.setTypeRdv(cbType.getValue());
		//rdv.setPaiement(paiement);
		
		/*try {
			MRdv.save(rdv);
		} catch (DbSaveException e) {
			e.printStackTrace();
		}*/
		
		if(isValid()){
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
		
		/*if(textFPrenom.getText() == null || textFPrenom.getText().length() == 0){
			errorMessage += "Champ Pr�nom invalid\n";
		}*/
		if(dpDate.getValue() == null){
			errorMessage += "Champ Date invalid\n";
		}
		if(spHeure.getValue() == null || spHeure.getValue() >= 24){
			errorMessage += "Champ Heure invalid\n";
		}
		if(spMinute.getValue() == null || spMinute.getValue() >= 60){
			errorMessage += "Champ Minute invalid\n";
		}
		if(payment == null){
			errorMessage += "Paiement non renseign�\n";
		}
		System.out.println(spMinute.getValue());
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
