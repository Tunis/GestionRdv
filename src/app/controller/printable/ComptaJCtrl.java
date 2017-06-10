package app.controller.printable;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import models.compta.ComptaJPaiement;
import models.compta.ComptaJournaliere;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ComptaJCtrl implements Initializable {
	@FXML
	private Label lblMedecin;

	@FXML
	private Label lblDate;

	@FXML
	private Label nbCS;

	@FXML
	private Label nbC2;

	@FXML
	private Label nbECHO;

	@FXML
	private Label nbDIU;

	@FXML
	private GridPane cj;

	@FXML
	private Label lblTotalDate;

	@FXML
	private Label lblTpNb;

	@FXML
	private Label lblTpMontant;

	@FXML
	private Label lblEspeceNb;

	@FXML
	private Label lblEspeceMontant;

	@FXML
	private Label lblCbNb;

	@FXML
	private Label lblCbMontant;

	@FXML
	private Label lblChequeNb;

	@FXML
	private Label lblChequeMontant;

	@FXML
	private Label lblImpayer;

	private ComptaJournaliere data;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}


	public void initData(ComptaJournaliere cj) {
		this.data = cj;
		lblDate.setText(cj.getDate().format(DateTimeFormatter.ofPattern("EEEE dd MMMM u")));
		lblMedecin.setText(cj.getMedecin().toString());


		setTotalData();
		setListData();
	}

	private void setTotalData() {

		nbC2.setText("" + data.getNbC2());
		nbCS.setText("" + data.getNbCS());
		nbECHO.setText("" + data.getNbEcho());
		nbDIU.setText("" + data.getNbDIU());

		lblTotalDate.setText(data.getDate().format(DateTimeFormatter.ofPattern("dd/MM/u")));
		lblTpNb.setText("" + data.getNbTp());
		lblTpMontant.setText("" + data.getTp());
		lblEspeceNb.setText("" + data.getNbEspece());
		lblEspeceMontant.setText("" + data.getEspece());
		lblCbNb.setText("" + data.getNbCB());
		lblCbMontant.setText("" + data.getCB());
		lblChequeNb.setText("" + data.getNbCheque());
		lblChequeMontant.setText("" + data.getCheque());
		lblImpayer.setText("" + data.getImpayer());
	}

	private void setListData() {
		int index = 2;
		for (ComptaJPaiement p : data.getPaiementList()) {
			Label heure = new Label(p.getRdv().getTime().toString());
			heure.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			heure.getStyleClass().add("leftRow");
			heure.setAlignment(Pos.CENTER);
			Label nom = new Label(p.getRdv().getPatient().getLastName() != null ? p.getRdv().getPatient().getLastName() : "");
			nom.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			nom.getStyleClass().add("middleRow");
			nom.setAlignment(Pos.CENTER);
			Label nomJF = new Label(p.getRdv().getPatient().getMaidenName());
			nomJF.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			nomJF.getStyleClass().add("middleRow");
			nomJF.setAlignment(Pos.CENTER);
			Label prenom = new Label(p.getRdv().getPatient().getFirstName());
			prenom.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			prenom.getStyleClass().add("middleRow");
			prenom.setAlignment(Pos.CENTER);
			Label type = new Label(p.getRdv().getTypeRdv().toString());
			type.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			type.getStyleClass().add("middleRow");
			type.setAlignment(Pos.CENTER);
			Label cotation = new Label(p.getRdv().getCotation());
			cotation.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			cotation.getStyleClass().add("middleRow");
			cotation.setAlignment(Pos.CENTER);
			Label Tp = new Label(p.getTp() != null ? "" + p.getTp().getMontant() : "");
			Tp.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			Tp.getStyleClass().add("middleRow");
			Tp.setAlignment(Pos.CENTER);
			Label Espece = new Label("" + p.getEspece());
			Espece.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			Espece.getStyleClass().add("middleRow");
			Espece.setAlignment(Pos.CENTER);
			Label Cb = new Label("" + p.getCb());
			Cb.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			Cb.getStyleClass().add("middleRow");
			Cb.setAlignment(Pos.CENTER);
			Label Cmontant = new Label(p.getCheque() != null ? "" + p.getCheque().getMontant() : "");
			Cmontant.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			Cmontant.getStyleClass().add("middleRow");
			Cmontant.setAlignment(Pos.CENTER);
			Label CNom = new Label(p.getCheque() != null ? p.getCheque().getName() : "");
			CNom.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			CNom.getStyleClass().add("middleRow");
			CNom.setAlignment(Pos.CENTER);
			Label CBanque = new Label(p.getCheque() != null ? p.getCheque().getBanque() : "");
			CBanque.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			CBanque.getStyleClass().add("middleRow");
			CBanque.setAlignment(Pos.CENTER);
			Label Impayer;
			if (p.isPayer()) {
				Impayer = new Label("");
			} else {
				float imp = p.getPrix() - p.getEspece() - p.getCb() - (p.getTp() != null ? p.getTp().getMontant() : 0) - (p.getCheque() != null ? p.getCheque().getMontant() : 0);
				Impayer = new Label("" + imp);
			}
			Impayer.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			Impayer.getStyleClass().add("rightRow");
			Impayer.setAlignment(Pos.CENTER);

			cj.addRow(index, heure, nom, nomJF, prenom, type, cotation, Tp, Espece, Cb, Cmontant, CNom, CBanque, Impayer);
			index++;
		}
	}
}
