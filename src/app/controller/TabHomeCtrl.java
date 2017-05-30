package app.controller;

import app.Main;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import metier.action.MMedecin;
import metier.action.MPatient;
import metier.hibernate.data.exceptions.DbDeleteException;
import models.Medecin;
import models.Patient;


public class TabHomeCtrl {
	
	private Main mainApp;
	private MPatient mPatient;
	private MMedecin mMedecin;
	
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
    @FXML
    private Label testPatient;

    //Main m�thods
    //-----------------------------------
    public void setMainApp(Main mainApp, MPatient mPatient, MMedecin mMedecin) {
        this.mPatient = mPatient;
        this.mMedecin = mMedecin;
        this.mainApp = mainApp;

        cbBoxMedecin.itemsProperty().bind(mMedecin.listProperty());

        // pour l'autocompletion :
        //cbBoxPatient.setMetier(mPatient);
        cbBoxPatient.itemsProperty().bind(mPatient.listProperty());
        //TextFields.bindAutoCompletion(cbBoxPatient.getEditor(), cbBoxPatient.getItems());
        cbBoxPatient.itemsProperty().get().addListener(new ListChangeListener<Patient>() {
            @Override
            public void onChanged(Change<? extends Patient> c) {
                // mise a jour de l'autocompletion a chaque changement de la liste.
                //AutoCompletionBinding<Patient> ac = TextFields.bindAutoCompletion(cbBoxPatient.getEditor(), cbBoxPatient.getItems());
            }
        });

//        cbBoxPatient.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            // test pour voir la selection
//            testPatient.setText((newValue == null ? "" : newValue.toString()));
//        });
    }
    
    //Calls when buttons are click
    //-----------------------------------
    @FXML
    private void handleNewPatient() {
        mainApp.showCreatePatientDialog();
    }

    @FXML
    private void handleUpdatePatient() {    	
    	int selectedIndex = cbBoxPatient.getSelectionModel().getSelectedIndex();
        
        if(selectedIndex != -1){
        	mainApp.showProfilPatientDialog(cbBoxPatient.getItems().get(selectedIndex));
        }
    }
    
    @FXML
    private void handleDeletePatient() {
        //Patient selectedItem = cbBoxPatient.getSelectionModel().getSelectedItem();
        int selectedIndex = cbBoxPatient.getSelectionModel().getSelectedIndex();
        
        if(selectedIndex != -1){
        	Patient patient = cbBoxPatient.getItems().get(selectedIndex);
        	
	        try {
	            mPatient.delete(patient);
	        } catch (DbDeleteException e) {
	            Alert alert = new Alert(AlertType.ERROR);
	            alert.initOwner(mainApp.getPrimaryStage());
	            alert.setTitle("Maintenance");
	            alert.setHeaderText("Demande de Suppression du Patient");
	            alert.setContentText("erreur de suppression du patient");
	
	            alert.showAndWait();
	        }
        }
    }
    
    @FXML
    private void handleNewMedecin() {
        mainApp.showCreateMedecinDialog();
    }

    @FXML
    private void handleUpdateMedecin() {
    	int selectedIndex = cbBoxMedecin.getSelectionModel().getSelectedIndex();
        
        if(selectedIndex != -1){
        	
        }
    }
    
    @FXML
    private void handleDeleteMedecin() {
    	int selectedIndex = cbBoxMedecin.getSelectionModel().getSelectedIndex();
    	
        if(selectedIndex != -1){
        	Medecin selectedItem = cbBoxMedecin.getItems().get(selectedIndex);
        	
        	try{
	            mMedecin.delete(selectedItem);
	        } catch (DbDeleteException e) {
	            Alert alert = new Alert(AlertType.ERROR);
	            alert.initOwner(mainApp.getPrimaryStage());
	            alert.setTitle("Maintenance");
	            alert.setHeaderText("Demande de Suppression du Medecin");
	            alert.setContentText("SUPPRIME");
	
	            alert.showAndWait();
	        }
        }
    }
}
