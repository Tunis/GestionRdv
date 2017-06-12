package app.controller.dialog;

import app.util.AlerteUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import metier.action.MMedecin;
import metier.action.MPatient;
import metier.action.MRdv;
import metier.hibernate.data.exceptions.DbCreateException;
import metier.hibernate.data.exceptions.DbDuplicateException;
import metier.hibernate.data.exceptions.DbSaveException;
import models.Medecin;
import models.Patient;
import models.PresentDay;
import models.Rdv;
import models.enums.TypeRdv;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class CreateRdvDialogCtrl implements Initializable {
	private Stage dialogStage;
	private MPatient mPatient;
	private MMedecin mMedecin;
	private MRdv mRdv;
	private LocalDateTime dateRdv;
	private Medecin medecin;
    private Patient patient;

    @FXML
    private DatePicker dpDate;
    @FXML
    private TextField cbPatient;
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

    private AutoCompletionBinding<Patient> pAC;

    public void setDialogStage(Stage dialogStage, MPatient mPatient, MRdv mRdv, MMedecin mMedecin, LocalDateTime dateRdv, Medecin medecin) {
		this.dialogStage = dialogStage;
		this.mRdv = mRdv;
		this.mPatient = mPatient;
		this.mMedecin = mMedecin;
		this.medecin = medecin;
		this.dateRdv = dateRdv;
		
		displayRdv();

		dialogStage.getScene().setOnKeyReleased(ke -> {
			if (ke.getCode().equals(KeyCode.ENTER)) {
				handleCreateRdv();
			}
		});
	}
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
		spDuree.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(15, 120, 15, 15));
		spHeure.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23,8,1));
        spMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,0,15));

        cbType.setItems(FXCollections.observableArrayList(TypeRdv.values()));
    }
    
    @FXML
	private void handleCreateRdv() {
		if(isValid()){
			String cotation = textFCotation.getText();
			Duration duration = Duration.ofMinutes(spDuree.getValue());
			TypeRdv typeRdv = cbType.getValue();
			LocalTime time = LocalTime.of(spHeure.getValue(), spMinute.getValue());
			PresentDay presentDay = null;
			
			//Vérifie si le présentDay existe pour ce médecin
			Optional<PresentDay> isPresent = medecin.getPlannings().stream().filter(pDay -> pDay.getPresent().equals(dpDate.getValue())).findFirst();
			if(isPresent.isPresent()){
				presentDay = isPresent.get();
			} else {
                System.out.println("save medecin without present day");
                presentDay = new PresentDay(dpDate.getValue(), medecin);
				medecin.getPlannings().add(presentDay);
				
				try {
					mMedecin.save(medecin);
				} catch (DbSaveException e) {
					e.printStackTrace();
					AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_SAVE_DB, AlerteUtil.HEADERTEXT_INCORECT_FIELD, AlerteUtil.ERROR_MESSAGE_SAVE_DB);
				}
			}
	        Optional<Rdv> alreadyUse = presentDay.getRdvList().stream().filter(r ->
			        !(LocalTime.of(spHeure.getValue(), spMinute.getValue()).plusMinutes(spDuree.getValue()).isBefore(r.getTime().plusMinutes(1)) ||
					        LocalTime.of(spHeure.getValue(), spMinute.getValue()).isAfter(r.getTime().plusMinutes(r.getDuration().toMinutes()).minusMinutes(1)))
	        ).findAny();
	        if (alreadyUse.isPresent()) {
		        // TODO: 05/06/2017 date deja prise
		        // TODO: 05/06/2017 a reporter a chaque endroit ou la date peut etre modifier pour un rdv !
		        AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_INCORECT_FIELD, AlerteUtil.HEADERTEXT_INCORECT_FIELD, "La date est deja prise.");
		        return;
	        }
        	try {
        		mRdv.createRdv(cotation, duration, typeRdv, time, patient, presentDay);
			} catch (DbDuplicateException | DbCreateException e) {
				e.printStackTrace();
				AlerteUtil.showAlerte(dialogStage, AlerteUtil.TITLE_SAVE_DB, AlerteUtil.HEADERTEXT_INCORECT_FIELD, AlerteUtil.ERROR_MESSAGE_SAVE_DB);
			}
        	
        	dialogStage.close();
        }
    }

    private void displayRdv(){
		labelMedecin.setText(medecin.getFirstName() + " " + medecin.getLastName());
		dpDate.setValue(dateRdv.toLocalDate());
		spHeure.getValueFactory().setValue(dateRdv.getHour());
		spMinute.getValueFactory().setValue(dateRdv.getMinute());

        pAC = TextFields.bindAutoCompletion(cbPatient, mPatient.getList());
        pAC.setOnAutoCompleted(e -> {
            cbPatient.setText(e.getCompletion().toString());
            patient = e.getCompletion();
        });
        mPatient.listProperty().addListener((observable, oldValue, newValue) -> {
            pAC.dispose();
            pAC = TextFields.bindAutoCompletion(cbPatient, mPatient.getList());
            pAC.setOnAutoCompleted(e -> {
                cbPatient.setText(e.getCompletion().toString());
                patient = e.getCompletion();
            });
        });
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
        if (patient == null) {
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
