package app.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AlerteUtil {
	public static final String TITLE_INCORECT_FIELD = "Erreur de Renseignement";
	public static final String TITLE_CREATE_PATIENT_DB = "Patient déjà renseigné";
	public static final String TITLE_CREATE_MEDECIN_DB = "Medecin déjà renseigné";
	
	public static final String HEADERTEXT_INCORECT_FIELD = "Merci de modifier les champ incorrect";
	public static final String HEADERTEXT_CREATE_DB = "Données déjà éxistante en Base !";
	
	public static final String ERROR_MESSAGE_SAVE_DB = "Erreur lors de la Sauvegarde en Base";
	public static final String ERROR_MESSAGE_CREATE_PATIENT_DB = "Veuillez vérifier les champs, ou rechercher le patient directement";
	public static final String ERROR_MESSAGE_CREATE_MEDECIN_DB = "Veuillez vérifier les champs, ou rechercher le medecin directement";
	
	public static void showAlerte(Stage dialogStage, String title, String headerText, String errorMessage){
		// Show the error message.
        Alert alert = new Alert(AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(errorMessage);

        alert.showAndWait();
	}
}