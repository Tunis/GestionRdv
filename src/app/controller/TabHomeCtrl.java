package app.controller;

import app.Main;
import app.util.AlerteUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.print.PageOrientation;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import metier.action.MMedecin;
import metier.action.MPatient;
import metier.hibernate.data.exceptions.DbDeleteException;
import metier.hibernate.data.exceptions.DbGetException;
import metier.print.Printable;
import models.Medecin;
import models.Patient;
import models.PresentDay;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.time.LocalDate;
import java.util.Optional;


public class TabHomeCtrl {
	
	private Main mainApp;
	private MPatient mPatient;
	private MMedecin mMedecin;
	
	@FXML
    private TextField cbBoxMedecin;
    @FXML
    private TextField cbBoxPatient;

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

    private ObjectProperty<Patient> patient = new SimpleObjectProperty<>();
    private AutoCompletionBinding<Medecin> mAC;
    private AutoCompletionBinding<Patient> pAC;

    //Main methods
    //-----------------------------------
    public void setMainApp(Main mainApp, MPatient mPatient, MMedecin mMedecin) {
        this.mPatient = mPatient;
        this.mMedecin = mMedecin;
        this.mainApp = mainApp;

        mAC = TextFields.bindAutoCompletion(cbBoxMedecin, mMedecin.getList());
        mAC.setOnAutoCompleted(e -> {
            cbBoxMedecin.setText(e.getCompletion().toString());
            mainApp.medecinProperty().set(e.getCompletion());
        });
        mMedecin.listProperty().addListener((observable, oldValue, newValue) -> {
            mAC.dispose();
            mAC = TextFields.bindAutoCompletion(cbBoxMedecin, mMedecin.getList());
            mAC.setOnAutoCompleted(e -> {
                cbBoxMedecin.setText(e.getCompletion().toString());
                mainApp.medecinProperty().set(e.getCompletion());
            });
        });

        // pour l'autocompletion :
        pAC = TextFields.bindAutoCompletion(cbBoxPatient, mPatient.getList());
        pAC.setOnAutoCompleted(e -> {
            cbBoxPatient.setText(e.getCompletion().toString());
            patient.set(e.getCompletion());
        });
        mPatient.listProperty().addListener((observable, oldValue, newValue) -> {
            pAC.dispose();
            pAC = TextFields.bindAutoCompletion(cbBoxPatient, mPatient.getList());
            pAC.setOnAutoCompleted(e -> {
                cbBoxPatient.setText(e.getCompletion().toString());
                patient.set(e.getCompletion());
            });
        });

        mainApp.medecinProperty().addListener((observable, oldValue, newValue) -> {
            cbBoxMedecin.setText(mainApp.getMedecin().toString());
        });

    }
    
    //Calls when buttons are click
    //-----------------------------------
    @FXML
    private void handleNewPatient() {
        mainApp.showCreatePatientDialog();
    }

    @FXML
    private void handleUpdatePatient() {
        if (patient.isNotNull().get()) {
            mainApp.showProfilPatientDialog(patient.get());
        }
    }
    
    @FXML
    private void handleDeletePatient() {
        if (patient.isNotNull().get()) {

	        try {
                patient.get().getRdvList().forEach(r -> r.getPresentDay().getRdvList().remove(r));
                mPatient.delete(patient.get());
                patient.set(mPatient.getList().get(0));
                mMedecin.updateList();
                mPatient.updateList();
            } catch (DbDeleteException e) {
	            Alert alert = new Alert(AlertType.ERROR);
	            alert.initOwner(mainApp.getPrimaryStage());
	            alert.setTitle("Maintenance");
	            alert.setHeaderText("Demande de Suppression du Patient");
	            alert.setContentText("erreur de suppression du patient");

	            alert.showAndWait();
            } catch (DbGetException e) {
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void handleNewMedecin() {
        mainApp.showCreateMedecinDialog();
    }

    @FXML
    private void handleUpdateMedecin() {
        if (mainApp.medecinProperty().isNotNull().get()) {
            mainApp.showProfilMedecinDialog(mainApp.medecinProperty().get());
        }
    }
    
    @FXML
    private void handleDeleteMedecin() {
        if (mainApp.medecinProperty().isNotNull().get()) {
            try{
                mMedecin.delete(mainApp.medecinProperty().get());
                mainApp.medecinProperty().set(mMedecin.getList().get(0));
                mMedecin.updateList();
                mPatient.updateList();
            } catch (DbDeleteException e) {
	            Alert alert = new Alert(AlertType.ERROR);
	            alert.initOwner(mainApp.getPrimaryStage());
	            alert.setTitle("Maintenance");
	            alert.setHeaderText("Demande de Suppression du Medecin");
	            alert.setContentText("SUPPRIME");
	            alert.showAndWait();
            } catch (DbGetException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void printPlannings() {
        try {
            PrinterJob job = PrinterJob.createPrinterJob();
            job.showPrintDialog(mainApp.getPrimaryStage());

            for (Medecin medecin : mMedecin.getList()) {
                Optional<PresentDay> present = medecin.getPlannings().stream().filter(p -> p.getPresent().equals(LocalDate.now())).findFirst();
                if (present.isPresent())
                    Printable.print(job, Printable.createPlanning(present.get()), PageOrientation.PORTRAIT);
            }
        } catch (Exception ex) {
            AlerteUtil.showAlerte(mainApp.getPrimaryStage(), "Erreur d'impression", "Probleme lors de l'impression", "une erreur est survenu empechant l'impression de s'effectué.\n Veuillez reessayer, si le probleme persiste contacter l'administrateur.");
        }
    }
}
