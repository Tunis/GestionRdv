package app.view.cellFactory;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.Rdv;

public class RdvCell extends VBox {

    private Label patient = new Label();
    private Label cotation = new Label();

    public RdvCell(Rdv r) {
        if (r != null) {
            patient.setText(r.getPatient().showName());
            cotation.setText(r.getCotation());
        }
        this.setSpacing(5);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(patient, cotation);
    }
}
