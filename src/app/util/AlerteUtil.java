package app.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AlerteUtil {
	public static final String TITLE_INCORECT_FIELD = "Erreur de Renseignement";
	public static final String HEADERTEXT_INCORECT_FIELD = "Merci de modifier les champ incorrect";
	
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
