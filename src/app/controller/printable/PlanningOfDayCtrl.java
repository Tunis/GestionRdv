package app.controller.printable;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import models.PresentDay;
import models.Rdv;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class PlanningOfDayCtrl implements Initializable {

	@FXML
	private Label lblMedecin;

	@FXML
	private Label lblDate;
	@FXML
	private GridPane rdvList;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void initData(PresentDay planning) {
		lblDate.setText(planning.getPresent().format(DateTimeFormatter.ofPattern("EEEE dd MMMM u", Locale.getDefault())));
		lblMedecin.setText(planning.getMedecin().toString());

		planning.getRdvList().sort(Rdv::compareTo);
		genList(planning.getRdvList());
	}

	private void genList(List<Rdv> rdvs) {
		int index = 1;
		for (Rdv r : rdvs) {
			Label lblTime = new Label(r.getTime().toString());
			lblTime.setMinWidth(50);
			lblTime.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			lblTime.getStyleClass().add("leftRow");
			lblTime.setAlignment(Pos.CENTER);
			lblTime.setTextAlignment(TextAlignment.CENTER);
			Label lblNom = new Label(r.getPatient().getLastName() != null ? r.getPatient().getLastName() : "");
			lblNom.setMinWidth(100);
			lblNom.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			lblNom.getStyleClass().add("middleRow");
			lblNom.setAlignment(Pos.CENTER);
			lblNom.setTextAlignment(TextAlignment.CENTER);
			Label lblNomJF = new Label(r.getPatient().getMaidenName());
			lblNomJF.setMinWidth(100);
			lblNomJF.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			lblNomJF.getStyleClass().add("middleRow");
			lblNomJF.setAlignment(Pos.CENTER);
			lblNomJF.setTextAlignment(TextAlignment.CENTER);
			Label lblPrenom = new Label(r.getPatient().getFirstName());
			lblPrenom.setMinWidth(100);
			lblPrenom.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			lblPrenom.getStyleClass().add("middleRow");
			lblPrenom.setAlignment(Pos.CENTER);
			lblPrenom.setTextAlignment(TextAlignment.CENTER);
			Label lblCotation = new Label(r.getCotation());
			lblCotation.setMinWidth(100);
			lblCotation.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			lblCotation.getStyleClass().add("rightRow");
			lblCotation.setTextAlignment(TextAlignment.CENTER);
			lblCotation.setAlignment(Pos.CENTER);
			rdvList.addRow(index, lblTime, lblNom, lblNomJF, lblPrenom, lblCotation);
			index++;
		}
	}
}
