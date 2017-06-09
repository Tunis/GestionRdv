package metier.print.models;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.compta.ComptaJPaiement;
import models.compta.ComptaJournaliere;
import models.enums.TypeRdv;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PlanningCtrl implements Initializable {
    @FXML
    private Label lblMedecin;

    @FXML
    private Label lblDate;

    @FXML
    private TableView<ComptaJPaiement> tblJour;

    @FXML
    private TableColumn<ComptaJPaiement, LocalTime> colTime;

    @FXML
    private TableColumn colPatient;

    @FXML
    private TableColumn<ComptaJPaiement, String> colPatientNom;

    @FXML
    private TableColumn<ComptaJPaiement, String> colPatientNomJF;

    @FXML
    private TableColumn<ComptaJPaiement, String> colPatientPrenom;

    @FXML
    private TableColumn<ComptaJPaiement, TypeRdv> ColCS;

    @FXML
    private TableColumn<ComptaJPaiement, String> colCotation;

    @FXML
    private TableColumn<ComptaJPaiement, Float> ColTP;

    @FXML
    private TableColumn<ComptaJPaiement, Float> colEspece;

    @FXML
    private TableColumn<ComptaJPaiement, Float> colCB;

    @FXML
    private TableColumn colCheque;

    @FXML
    private TableColumn<ComptaJPaiement, Float> colMontantCheque;

    @FXML
    private TableColumn<ComptaJPaiement, String> colBanqueCheque;

    @FXML
    private TableColumn<ComptaJPaiement, String> colNomCheque;

    @FXML
    private TableColumn<ComptaJPaiement, Float> colImpayer;

    @FXML
    private Label nbCS;

    @FXML
    private Label nbC2;

    @FXML
    private Label nbECHO;

    @FXML
    private Label nbDIU;

    @FXML
    private TableView<ComptaJournaliere> tblTotal;

    @FXML
    private TableColumn<ComptaJournaliere, LocalDate> colDate;

    @FXML
    private TableColumn ColTPTotal;

    @FXML
    private TableColumn<ComptaJournaliere, Integer> colTnbTP;

    @FXML
    private TableColumn<ComptaJournaliere, Float> colTMontantTP;

    @FXML
    private TableColumn colEspeceTotal;

    @FXML
    private TableColumn<ComptaJournaliere, Integer> colTNbEspece;

    @FXML
    private TableColumn<ComptaJournaliere, Float> colTMontantEspece;

    @FXML
    private TableColumn colCBTotal;

    @FXML
    private TableColumn<ComptaJournaliere, Integer> colTNbCB;

    @FXML
    private TableColumn<ComptaJournaliere, Float> colTMontantCB;

    @FXML
    private TableColumn colChequeTotal;

    @FXML
    private TableColumn<ComptaJournaliere, Integer> colTNbCheque;

    @FXML
    private TableColumn<ComptaJournaliere, Float> colTMontantCheque;

    @FXML
    private TableColumn<ComptaJournaliere, Float> colImpayerTotal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO: 09/06/2017 verif null pointer dans les cellFactory
        // define cellValueFactory
        colTime.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getRdv().getTime()));
        colPatientNom.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getRdv().getPatient().getLastName()));
        colPatientNomJF.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getRdv().getPatient().getMaidenName()));
        colPatientPrenom.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getRdv().getPatient().getFirstName()));

        ColCS.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().getRdv().getTypeRdv()));

        colCotation.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getRdv().getCotation()));
        ColTP.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().getTp().getMontant()));
        colEspece.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().getEspece()));
        colCB.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().getCb()));
        colMontantCheque.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().getCheque().getMontant()));
        colBanqueCheque.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().getCheque().getBanque()));
        colNomCheque.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().getCheque().getName()));
        colImpayer.setCellValueFactory(d -> {
            ComptaJPaiement p = d.getValue();
            if (p.isPayer()) {
                return new ReadOnlyObjectWrapper<>(0f);
            } else {
                return new ReadOnlyObjectWrapper<>(
                        p.getPrix() -
                                p.getEspece() -
                                p.getCb() -
                                (p.getTp() != null ? p.getTp().getMontant() : 0) -
                                (p.getCheque() != null ? p.getCheque().getMontant() : 0)
                );
            }
        });


        colDate.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().getDate()));

        colTnbTP.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().getNbTp()));
        colTNbEspece.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().getNbEspece()));
        colTNbCB.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().getNbCB()));
        colTNbCheque.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().getNbCheque()));

        colTMontantEspece.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().getEspece()));
        colTMontantTP.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().getTp()));
        colTMontantCB.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().getCB()));
        colTMontantCheque.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().getCheque()));
    }


    public void initData(ComptaJournaliere cj) {
        lblDate.setText(cj.getDate().format(DateTimeFormatter.BASIC_ISO_DATE));
        lblMedecin.setText(cj.getMedecin().toString());

        tblJour.setItems(FXCollections.observableArrayList(cj.getPaiementList()));
        tblTotal.setItems(FXCollections.observableArrayList(cj));

        nbC2.setText("" + cj.getNbC2());
        nbCS.setText("" + cj.getNbCS());
        nbECHO.setText("" + cj.getNbEcho());
        nbDIU.setText("" + cj.getNbDIU());
    }
}
