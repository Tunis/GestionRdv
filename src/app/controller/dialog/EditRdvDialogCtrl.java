package app.controller.dialog;

import app.Main;
import app.util.AlerteUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import metier.action.MMedecin;
import metier.action.MRdv;
import metier.hibernate.data.exceptions.DbSaveException;
import models.Medecin;
import models.PresentDay;
import models.Rdv;
import models.enums.TypeRdv;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Optional;

public class EditRdvDialogCtrl {
	private Stage dialogStage;
	private MMedecin mMedecin;
	private MRdv mRdv;
	private Rdv rdv;
	private ProfilPatientDialogCtrl patientCtrl;
	private Main mainApp;
    private Medecin medecin;

    @FXML
    private DatePicker dpDate;
	@FXML
    private Button labNamePatient;
    @FXML
    private TextField cbMedecin;
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
    private Button btnPayment;

    private AutoCompletionBinding<Medecin> mAC;
    
	public void setDialogStage(Stage dialogStage, Rdv rdv, MMedecin mMedecin, MRdv mRdv, Main mainApp, ProfilPatientDialogCtrl patientCtrl) {
		this.mMedecin = mMedecin;
		this.dialogStage = dialogStage;
		this.mRdv = mRdv;
		this.rdv = rdv;
		this.patientCtrl = patientCtrl;
		this.mainApp = mainApp;

        if (rdv.getPaiement() != null && rdv.getPaiement().getDate() != null) {
            btnPayment.setDisable(true);
            btnPayment.setTooltip(new Tooltip(rdv.getPaiement() != null ? "" + rdv.getPaiement().getPrix() : ""));
        } else {
            btnPayment.setTooltip(new Tooltip(rdv.getPaiement() != null ? "" + rdv.getPaiement().getPrix() : ""));
        }
        displayRdv();
    }
	
	//Affiche le RdV
	private void displayRdv(){
		
		dpDate.setValue(rdv.getPresentDay().getPresent());
        labNamePatient.setText(rdv.getPatient().toString());

        cbMedecin.setText(rdv.getPresentDay().getMedecin().toString());
        medecin = rdv.getPresentDay().getMedecin();

        mAC = TextFields.bindAutoCompletion(cbMedecin, mMedecin.getList());
        mAC.setOnAutoCompleted(e -> {
            cbMedecin.setText(e.getCompletion().toString());
            medecin = e.getCompletion();
        });
        mMedecin.listProperty().addListener((observable, oldValue, newValue) -> {
            mAC.dispose();
            mAC = TextFields.bindAutoCompletion(cbMedecin, mMedecin.getList());
            mAC.setOnAutoCompleted(e -> {
                cbMedecin.setText(e.getCompletion().toString());
                medecin = e.getCompletion();
            });
        });

        spDuree.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(15,120,30,15));
		spDuree.getValueFactory().setValue((int) rdv.getDuration().toMinutes());
		
        spHeure.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23,8,1));
        spHeure.getValueFactory().setValue(rdv.getTime().getHour());
        
        
        spMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,0,15));
        spMinute.getValueFactory().setValue(rdv.getTime().getMinute());
        
		cbType.setItems(FXCollections.observableArrayList(TypeRdv.values()));
		cbType.setValue(rdv.getTypeRdv());
		
		textFCotation.setText(rdv.getCotation());
	}
	
	@FXML
	public void handleUpadte(){
		
		if(isValid()){

            PresentDay presentDay;
            //changement de medecin ou non?
            if (!rdv.getPresentDay().getMedecin().equals(medecin)) {
                // changement du medecin pour le rdv :
                // verif si medecin a deja ce present day
                Optional<PresentDay> isPresent = medecin.getPlannings().stream().filter(pDay -> pDay.getPresent().equals(dpDate.getValue())).findFirst();
                if (isPresent.isPresent()) {
                    // deja present on le recupere
                    presentDay = isPresent.get();
                } else {
                    // pas present donc on cree le present day et l'ajoute au medecin
                    presentDay = new PresentDay(dpDate.getValue(), medecin);
                    medecin.getPlannings().add(presentDay);
                    try {
                        mMedecin.save(medecin);
                    } catch (DbSaveException e) {
                        e.printStackTrace();
                        AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_SAVE_DB, AlerteUtil.HEADERTEXT_INCORECT_FIELD, AlerteUtil.ERROR_MESSAGE_SAVE_DB);
                    }
                }
            } else {
                // pas de changement.
                presentDay = rdv.getPresentDay();
            }
            // verifier si la nouvelle date est dispo
            Optional<PresentDay> present = presentDay.getMedecin().getPlannings().stream().filter(p -> p.getPresent().equals(dpDate.getValue())).findFirst();
            if (present.isPresent()) {
					Optional<Rdv> alreadyUse = present.get().getRdvList().stream().filter(r ->
							!(LocalTime.of(spHeure.getValue(), spMinute.getValue()).plusMinutes(spDuree.getValue()).isBefore(r.getTime().plusMinutes(1)) ||
									LocalTime.of(spHeure.getValue(), spMinute.getValue()).isAfter(r.getTime().plusMinutes(r.getDuration().toMinutes()).minusMinutes(1))) && !rdv.equals(r)
					).findAny();
					if (!alreadyUse.isPresent()) {
						rdv.getPresentDay().getRdvList().remove(rdv);
						rdv.setPresentDay(present.get());
						present.get().getRdvList().add(rdv);
					} else {
						AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_INCORECT_FIELD, AlerteUtil.HEADERTEXT_INCORECT_FIELD, "La date est deja prise.");
						return;
					}
				} else {
                    rdv.setPresentDay(new PresentDay(dpDate.getValue(), presentDay.getMedecin()));
                rdv.getPresentDay().getMedecin().getPlannings().add(rdv.getPresentDay());
				}

			rdv.setDuration(Duration.ofMinutes(spDuree.getValue()));
			rdv.setCotation(textFCotation.getText());
			rdv.setTime(LocalTime.of(Integer.valueOf(spHeure.getEditor().getText()), Integer.valueOf(spMinute.getEditor().getText())));
			rdv.setTypeRdv(cbType.getValue());

            if (rdv.getPaiement() != null) {
                rdv.getPaiement().setMedecin(rdv.getPresentDay().getMedecin());
            }

            try {
				mRdv.save(rdv);
			} catch (DbSaveException e) {
				e.printStackTrace();
				AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_SAVE_DB, AlerteUtil.HEADERTEXT_CREATE_DB, AlerteUtil.ERROR_MESSAGE_SAVE_DB);
			}
			
			if(patientCtrl != null)
				patientCtrl.geTableView().refresh();
			dialogStage.close();
		}
	}
	
	@FXML
	public void handlePayment(){
		//TODO : Affiche le pop-up pour renseigner le paiement
		//Besoin d'un retour paiement??
		mainApp.showPaiementDialog(rdv);
		try {
			mRdv.save(rdv);
		} catch (DbSaveException e) {
			e.printStackTrace();
		}
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
		if (errorMessage.length() == 0) {
            return true;
        } else {
            AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_INCORECT_FIELD, AlerteUtil.HEADERTEXT_INCORECT_FIELD, errorMessage);

            return false;
        }
	}

    public void handleDelRdv(ActionEvent event) {
        // TODO: 07/06/2017 a faire
        try {
            PresentDay presentDay = rdv.getPresentDay();
            presentDay.getRdvList().remove(rdv);
            rdv.setPresentDay(null);
            rdv.getPatient().getRdvList().remove(rdv);
            rdv.setPatient(null);
            mMedecin.save(presentDay.getMedecin());
            //mRdv.delete(rdv);
            dialogStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPatient(ActionEvent event) {
        mainApp.showProfilPatientDialog(rdv.getPatient());
    }
}
