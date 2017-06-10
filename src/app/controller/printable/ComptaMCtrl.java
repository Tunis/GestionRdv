package app.controller.printable;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import models.compta.ComptaJournaliere;
import models.compta.ComptaMensuelle;

import java.time.format.DateTimeFormatter;

public class ComptaMCtrl {
	@FXML
	private Label lblMedecin;

	@FXML
	private Label lblDate;

	@FXML
	private GridPane listCj;

	@FXML
	private Label tDate;

	@FXML
	private Label tCS;

	@FXML
	private Label tC2;

	@FXML
	private Label tEcho;

	@FXML
	private Label tDIU;

	@FXML
	private Label tTp;

	@FXML
	private Label tEspece;

	@FXML
	private Label tCB;

	@FXML
	private Label tCheque;

	@FXML
	private Label tImpayer;

	@FXML
	private Label tSolde;

	@FXML
	private Label tRetrait;

	@FXML
	private Label tNbTp;

	@FXML
	private Label tNbEspece;

	@FXML
	private Label tNbCB;

	@FXML
	private Label tNbCheque;
	private ComptaMensuelle cm;

	public void initData(ComptaMensuelle cm) {
		this.cm = cm;

		lblMedecin.setText(cm.getMedecin().toString());
		lblDate.setText(cm.getDate().format(DateTimeFormatter.ofPattern("MMMM uuuu")));

		setList();
		setTotal();
	}

	private void setTotal() {
		tDate.setText(cm.getDate().format(DateTimeFormatter.ofPattern("MMMM uuuu")));
		tCS.setText("" + cm.getNbCS());
		tC2.setText("" + cm.getNbC2());
		tEcho.setText("" + cm.getNbEcho());
		tDIU.setText("" + cm.getNbDIU());

		tNbTp.setText("" + cm.getNbTp());
		tTp.setText("" + cm.getTp());
		tNbEspece.setText("" + cm.getNbEspece());
		tEspece.setText("" + cm.getEspece());
		tNbCB.setText("" + cm.getNbCB());
		tCB.setText("" + cm.getCB());
		tNbCheque.setText("" + cm.getNbCheque());
		tCheque.setText("" + cm.getCheque());
		tImpayer.setText("" + cm.getImpayer());
		tSolde.setText("" + cm.getsolde());
		tRetrait.setText("" + cm.getRetrait());
	}

	private void setList() {
		int index = 2;
		for (ComptaJournaliere cj : cm.getComptaJournaliere()) {
			Label date = new Label(cj.getDate().format(DateTimeFormatter.ofPattern("dd/MM/u")));
			date.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			date.getStyleClass().add("leftRow");
			date.setAlignment(Pos.CENTER);
			Label CS = new Label("" + cj.getNbCS());
			CS.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			CS.getStyleClass().add("middleRow");
			CS.setAlignment(Pos.CENTER);
			Label C2 = new Label("" + cj.getNbC2());
			C2.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			C2.getStyleClass().add("middleRow");
			C2.setAlignment(Pos.CENTER);
			Label ECHO = new Label("" + cj.getNbEcho());
			ECHO.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			ECHO.getStyleClass().add("middleRow");
			ECHO.setAlignment(Pos.CENTER);
			Label DIU = new Label("" + cj.getNbDIU());
			DIU.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			DIU.getStyleClass().add("middleRow");
			DIU.setAlignment(Pos.CENTER);
			Label nbTp = new Label("" + cj.getNbTp());
			nbTp.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			nbTp.getStyleClass().add("middleRow");
			nbTp.setAlignment(Pos.CENTER);
			Label TP = new Label("" + cj.getTp());
			TP.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			TP.getStyleClass().add("middleRow");
			TP.setAlignment(Pos.CENTER);
			Label nbEspece = new Label("" + cj.getNbEspece());
			nbEspece.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			nbEspece.getStyleClass().add("middleRow");
			nbEspece.setAlignment(Pos.CENTER);
			Label Espece = new Label("" + cj.getEspece());
			Espece.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			Espece.getStyleClass().add("middleRow");
			Espece.setAlignment(Pos.CENTER);
			Label nbCB = new Label("" + cj.getNbCB());
			nbCB.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			nbCB.getStyleClass().add("middleRow");
			nbCB.setAlignment(Pos.CENTER);
			Label CB = new Label("" + cj.getCB());
			CB.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			CB.getStyleClass().add("middleRow");
			CB.setAlignment(Pos.CENTER);
			Label nbCheque = new Label("" + cj.getNbCheque());
			nbCheque.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			nbCheque.getStyleClass().add("middleRow");
			nbCheque.setAlignment(Pos.CENTER);
			Label Cheque = new Label("" + cj.getCheque());
			Cheque.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			Cheque.getStyleClass().add("middleRow");
			Cheque.setAlignment(Pos.CENTER);
			Label Impayer = new Label("" + cj.getImpayer());
			Impayer.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			Impayer.getStyleClass().add("middleRow");
			Impayer.setAlignment(Pos.CENTER);
			Label Solde = new Label("" + cj.getSoldePrecedent());
			Solde.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			Solde.getStyleClass().add("middleRow");
			Solde.setAlignment(Pos.CENTER);
			Label Retrait = new Label("" + cj.getRetrait());
			Retrait.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			Retrait.getStyleClass().add("rightRow");
			Retrait.setAlignment(Pos.CENTER);

			listCj.addRow(index, date, CS, C2, ECHO, DIU, nbTp, TP, nbEspece, Espece, nbCB, CB, nbCheque, Cheque, Impayer, Solde, Retrait);
			index++;
		}
	}
}
