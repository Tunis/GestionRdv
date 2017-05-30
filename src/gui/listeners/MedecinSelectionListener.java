package gui.listeners;

import gui.RootCtrl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import metier.hibernate.data.DataMedecin;
import models.Medecin;

public class MedecinSelectionListener implements ChangeListener<Medecin> {

    @SuppressWarnings("unused")
	private DataMedecin dm;
    private RootCtrl rc;

    public MedecinSelectionListener(RootCtrl rootCtrl, DataMedecin dm) {
        rc = rootCtrl;
        this.dm = dm;
    }

    @Override
    public void changed(ObservableValue<? extends Medecin> observable, Medecin oldValue, Medecin newValue) {
        if(newValue != null) {
            rc.getmSelected().setText(newValue.getFirstName());
            newValue.setEmail("modifUi@fred.fred");
            //dm.saveEntity(newValue);
        }else{
            rc.getmSelected().setText("");
        }
        if(oldValue != null) {
            //dm.saveEntity(oldValue);
        }
    }
}
