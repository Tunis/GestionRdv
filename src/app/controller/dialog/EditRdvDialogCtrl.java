package app.controller.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Medecin;
import models.Patient;
import models.Rdv;
import models.TypeRdv;

public class EditRdvDialogCtrl {
	private Stage dialogStage;
	private Rdv rdv;
	
	@FXML
    private DatePicker dpDate;
    @FXML
    private ComboBox<Patient> cbPatient;
    @FXML
    private ComboBox<Medecin> cbMedecin;
    @FXML
    private Spinner<Integer> spHeure;
    @FXML
    private Spinner<Integer> spMinute;
    @FXML
    private Spinner<Integer> spDuree;
    @FXML
    private ComboBox<TypeRdv> cbType;
    @FXML
    private TextField textFCotation;
    
	public void setDialogStage(Stage dialogStage, Rdv rdv) {
		this.dialogStage = dialogStage;
		this.rdv = rdv;
    }
}
