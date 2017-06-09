package app.view.cellFactory;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import models.Paiement;

import java.time.format.DateTimeFormatter;

/*
    affiche la date, le patient, le medecin et le montant du paiement
 */

public class PaiementCell extends HBox {

    private Label date = new Label();
    private Label patient = new Label();
    private Label medecin = new Label();
    private Label montant = new Label();

    public PaiementCell(Paiement p) {
        date.setText(p.getRdv().getPresentDay().getPresent().format(DateTimeFormatter.ISO_LOCAL_DATE));
        patient.setText(p.getRdv().getPatient().showName());
        medecin.setText(p.getMedecin().showName());
        montant.setText("" + p.getPrix());
        this.setSpacing(10);
        this.getChildren().addAll(date, patient, medecin, montant);
    }
}
