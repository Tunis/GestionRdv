package app.controller.dialog;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Medecin;
import models.Patient;
import models.TypeRdv;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateRdvDialogCtrl implements Initializable {
	private Stage dialogStage;
	
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

    public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
    }
    
    @FXML
    private void handleCreateRdv(ActionEvent event) {
        // TODO: 29/05/2017 a faire
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        spDuree.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(15,120,30,15));
        spHeure.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23,8,1));
        spMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,0,15));

        cbType.setItems(FXCollections.observableArrayList(TypeRdv.values()));
    }
}
