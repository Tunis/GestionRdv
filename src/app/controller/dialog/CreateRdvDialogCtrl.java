package app.controller.dialog;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import metier.action.MPatient;
import models.Medecin;
import models.Patient;
import models.TypeRdv;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import app.util.AlerteUtil;

public class CreateRdvDialogCtrl implements Initializable {
	private Stage dialogStage;
	private MPatient mPatient;
	private LocalDateTime dateRdv;
	private Medecin medecin;
	
    @FXML
    private DatePicker dpDate;
    
    @FXML
    private ComboBox<Patient> cbPatient;
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
    private Label labelMedecin;

    public void setDialogStage(Stage dialogStage, MPatient mPatient, LocalDateTime dateRdv, Medecin medecin) {
		this.dialogStage = dialogStage;
		this.mPatient = mPatient;
		this.medecin = medecin;
		this.dateRdv = dateRdv;
		
		displayRdv();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        spDuree.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(15,120,30,15));
        spHeure.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23,8,1));
        spMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,0,15));

        cbType.setItems(FXCollections.observableArrayList(TypeRdv.values()));
    }
    
    @FXML
    private void handleCreateRdv(ActionEvent event) {
        if(isValid()){
        	System.out.println("Submit !");
        	/*if(!rdv.getPresentDay().getPresent().equals(dpDate.getValue())){
				rdv.setPresentDay(new PresentDay(dpDate.getValue(), rdv.getPresentDay().getMedecin()));
			}
			rdv.setDuration(Duration.ofMinutes(spDuree.getValue()));
			rdv.setCotation(textFCotation.getText());
			rdv.setTime(LocalTime.of(Integer.valueOf(spHeure.getEditor().getText()), Integer.valueOf(spMinute.getEditor().getText())));
			rdv.setTypeRdv(cbType.getValue());/*
			
			
			//TODO : Faire la save dans la Base
			/*try {
				MRdv.save(rdv);
			} catch (DbSaveException e) {
				e.printStackTrace();
			}*/
        	
        	dialogStage.close();
        }
    }

    private void displayRdv(){
    	cbPatient.setItems(FXCollections.observableArrayList(mPatient.listProperty()));
		labelMedecin.setText(medecin.getFirstName() + " " + medecin.getLastName());
		dpDate.setValue(dateRdv.toLocalDate());
		spHeure.getValueFactory().setValue(dateRdv.getHour());
		spMinute.getValueFactory().setValue(dateRdv.getMinute());
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
		if(cbPatient.getValue() == null){
			errorMessage += "Patient non séléctionné\n";
		}
		if(textFCotation.getText() == null || textFCotation.getText().length() == 0){
			errorMessage += "Cotation non renseigné\n";
		}
		if(cbType.getValue() == null){
			errorMessage += "Type rdv non séléctionné\n";
		}
		if (errorMessage.length() == 0) {
	          return true;
	      } else {
	          AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_INCORECT_FIELD, AlerteUtil.HEADERTEXT_INCORECT_FIELD, errorMessage);
	
	          return false;
	      }
	}
}
