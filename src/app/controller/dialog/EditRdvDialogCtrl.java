package app.controller.dialog;

import app.Main;
import app.util.AlerteUtil;
import javafx.collections.FXCollections;
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
    
	public void setDialogStage(Stage dialogStage, Rdv rdv, MMedecin mMedecin, MRdv mRdv, Main mainApp, ProfilPatientDialogCtrl patientCtrl) {
		this.mMedecin = mMedecin;
		this.dialogStage = dialogStage;
		this.mRdv = mRdv;
		this.rdv = rdv;
		this.patientCtrl = patientCtrl;
		this.mainApp = mainApp;

		if (rdv.getPaiement() != null && rdv.getPaiement().getDate() != null)
			btnPayment.setDisable(true);
		displayRdv();
    }
	
	//Affiche le RdV
	private void displayRdv(){
		
		dpDate.setValue(rdv.getPresentDay().getPresent());
		labNamePatient.setText(rdv.getPatient().getMaidenName());
		
		cbMedecin.itemsProperty().bind(mMedecin.listProperty());
		cbMedecin.setValue(rdv.getPresentDay().getMedecin());
		
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

			//update Object Rdv
			if(!rdv.getPresentDay().getPresent().equals(dpDate.getValue())){
				// TODO: 03/06/2017 changement de date, aller voir si le medecin est dispo se jour la sinon cree le presentDay et l'ajouter a tout le monde.
				Optional<PresentDay> present = rdv.getPresentDay().getMedecin().getPlannings().stream().filter(p -> p.getPresent().equals(dpDate.getValue())).findFirst();
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
						// TODO: 05/06/2017 date deja prise
						AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_INCORECT_FIELD, AlerteUtil.HEADERTEXT_INCORECT_FIELD, "La date est deja prise.");
						return;
					}
				} else {
					rdv.setPresentDay(new PresentDay(dpDate.getValue(), rdv.getPresentDay().getMedecin()));
					rdv.getPresentDay().getMedecin().getPlannings().add(rdv.getPresentDay());
				}
			}
			Optional<Rdv> alreadyUse = rdv.getPresentDay().getRdvList().stream().filter(r ->
					!(LocalTime.of(spHeure.getValue(), spMinute.getValue()).plusMinutes(spDuree.getValue()).isBefore(r.getTime().plusMinutes(1)) ||
							LocalTime.of(spHeure.getValue(), spMinute.getValue()).isAfter(r.getTime().plusMinutes(r.getDuration().toMinutes()).minusMinutes(1))) && !rdv.equals(r)
			).findAny();
			System.out.println("already in use : " + alreadyUse.isPresent());
			if (alreadyUse.isPresent()) {
				// TODO: 05/06/2017 date deja prise
				// TODO: 05/06/2017 a reporter a chaque endroit ou la date peut etre modifier pour un rdv ! 
				AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_INCORECT_FIELD, AlerteUtil.HEADERTEXT_INCORECT_FIELD, "La date est deja prise.");
				return;
			}

			rdv.setDuration(Duration.ofMinutes(spDuree.getValue()));
			rdv.setCotation(textFCotation.getText());
			rdv.setTime(LocalTime.of(Integer.valueOf(spHeure.getEditor().getText()), Integer.valueOf(spMinute.getEditor().getText())));
			rdv.setTypeRdv(cbType.getValue());
			
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
}
