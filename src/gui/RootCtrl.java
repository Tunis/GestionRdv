package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import metier.hibernate.data.DataMedecin;
import metier.hibernate.data.DataPatient;
import models.Medecin;
import models.Patient;

import java.net.URL;
import java.util.ResourceBundle;

public class RootCtrl implements Initializable {

    @FXML
    private TextField searchP;
    @FXML
    private TextField searchM;
    @FXML
    private ListView<Medecin> listMedecin;
    @FXML
    private Label mSelected;
    @FXML
    private Label pSelected;
    @FXML
    private ListView<Patient> listPatient;

    private DataPatient dp = new DataPatient();
    private DataMedecin dm = new DataMedecin();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        listPatient.setItems(dp.getEntities());
//        listPatient.setCellFactory(new Callback<ListView<Patient>, ListCell<Patient>>() {
//            @Override
//            public ListCell<Patient> call(ListView<Patient> param) {
//                return new cellPatient();
//            }
//        });
//        listPatient.getSelectionModel().selectedItemProperty()
//                .addListener(new PatientSelectionListener(this, dp));
//
//        listMedecin.setItems(dm.getEntities());
//        listMedecin.setCellFactory(new Callback<ListView<Medecin>, ListCell<Medecin>>() {
//            @Override
//            public ListCell<Medecin> call(ListView<Medecin> param) {
//                return new cellMedecin();
//            }
//        });
//        listMedecin.getSelectionModel().selectedItemProperty()
//                .addListener(new MedecinSelectionListener(this, dm));

    }

//    public void search(KeyEvent keyEvent) {
//        TextField selected = (TextField)keyEvent.getSource();
//        if(selected.equals(searchM)){
//            // recherche medecin
//            String text = searchM.getText();
//            List<Medecin> mList = dm.getEntities().stream()
//                    .filter(m -> m.getFirstName().toLowerCase().contains(text) ||
//                            m.getLastName().toLowerCase().contains(text))
//                    .collect(Collectors.toList());
//            ObservableList<Medecin> tempM = FXCollections.observableArrayList(mList);
//            listMedecin.setItems(tempM);
//        }else{
//            // recherche patient
//            String text = searchP.getText();
//            List<Patient> pList = dp.getEntities().stream()
//                    .filter(p -> p.getFirstName().toLowerCase().contains(text) ||
//                            p.getLastName().toLowerCase().contains(text) ||
//                            p.getMaidenName().toLowerCase().contains(text))
//                    .collect(Collectors.toList());
//            ObservableList<Patient> tempP = FXCollections.observableArrayList(pList);
//            listPatient.setItems(tempP);
//        }
//    }


    static class cellPatient extends ListCell<Patient> {
        @Override
        public void updateItem(Patient item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item.getFirstName());
            }else{
                setText("");
            }
        }
    }
    static class cellMedecin extends ListCell<Medecin> {
        @Override
        public void updateItem(Medecin item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item.getFirstName());
                // to make a node the cell
                //setGraphic();
            }else{
                setText("");
            }
        }
    }

    public Label getpSelected() {
        return pSelected;
    }

    public Label getmSelected() {
        return mSelected;
    }
}
