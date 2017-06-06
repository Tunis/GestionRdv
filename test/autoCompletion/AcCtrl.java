package autoCompletion;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import metier.action.MMedecin;
import models.Medecin;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.util.ResourceBundle;

public class AcCtrl implements Initializable {

	@FXML
	private ComboBox<Medecin> cbBox;

	@FXML
	private ChoiceBox<Medecin> choiceBox;

	@FXML
	private TextField textField;

	@FXML
	private Label textFChoice;

	@FXML
	private Label cbBoxChoice;

	@FXML
	private Label choiceBoxChoice;
	private MMedecin mMedecin;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mMedecin = new MMedecin();

		cbBox.itemsProperty().bind(mMedecin.listProperty());
		cbBox.setEditable(true);
		choiceBox.itemsProperty().bind(mMedecin.listProperty());

		AutoCompletionBinding<Medecin> textFAC = TextFields.bindAutoCompletion(textField, mMedecin.getList());
		AutoCompletionBinding<Medecin> cbBoxAC = TextFields.bindAutoCompletion(cbBox.getEditor(), mMedecin.getList());
		//TextFields.bindAutoCompletion(choiceBox., mMedecin.getList());

		textFAC.setOnAutoCompleted(e -> {
			Medecin completion = e.getCompletion();
			textFChoice.setText(completion.toString());
		});

		cbBoxAC.setOnAutoCompleted(e -> {
			Medecin completion = e.getCompletion();
			cbBox.getSelectionModel().select(completion);
			//cbBoxChoice.setText(cbBox.getSelectionModel().getSelectedItem().toString());
		});

		cbBox.getSelectionModel().selectedItemProperty().addListener((obs, oV, nV) -> {
			System.out.println("entrer dans listener cbBox");
			System.out.println("observable : " + obs.getValue());
			System.out.println("old value : " + oV);
			System.out.println("new value : " + nV);
			if (nV != null) {
				cbBoxChoice.setText(nV.toString());
			}
			System.out.println("sorti du listener.");
		});


	}
}
