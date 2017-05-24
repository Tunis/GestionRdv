package app.controller;

import app.Main;
import app.models.Medecin;
import app.models.Patient;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class TabHomeCtrl {
	
	private Main mainApp;
	
	@FXML
    private ComboBox<Medecin> cbBoxMedecin;
	@FXML
    private ComboBox<Patient> cbBoxPatient;
	
	//Button
    //-----------------------------------
    @FXML
    private Button btnCreatePatient;
    @FXML
    private Button btnUpdatePatient;
    @FXML
    private Button btnDeletePatient;
    @FXML
    private Button btnCreateMedecin;
    @FXML
    private Button btnUpdateMedecin;
    @FXML
    private Button btnDeleteMedecin;
    
	//Main m�thods
    //-----------------------------------
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
    
    //Calls when buttons are click
    //-----------------------------------
    @FXML
    private void handleNewPatient() {
        mainApp.showCreatePatientDialog();
    }
    
    @FXML
    private void handleUpdatePatient() {
    }
    
    @FXML
    private void handleDeletePatient() {
    	cbBoxPatient.getSelectionModel().getSelectedItem();
    	
    	//TODO : Supprimer en base le patient
        Alert alert = new Alert(AlertType.ERROR);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Maintenance");
        alert.setHeaderText("Demande de Suppression du Patient");
        alert.setContentText("SUPPRIME");

        alert.showAndWait();
    }
    
    @FXML
    private void handleNewMedecin() {
        mainApp.showCreateMedecinDialog();
    }
    
    @FXML
    private void handleUpdateMedecin() {
    }
    
    @FXML
    private void handleDeleteMedecin() {
    	cbBoxMedecin.getSelectionModel().getSelectedItem();
    	
    	//TODO : Supprimer en base le Medecin
    	Alert alert = new Alert(AlertType.ERROR);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Maintenance");
        alert.setHeaderText("Demande de Suppression du Medecin");
        alert.setContentText("SUPPRIME");

        alert.showAndWait();
    }
    
	
}
