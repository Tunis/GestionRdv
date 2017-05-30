package gui.listeners;

import gui.RootCtrl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import metier.hibernate.data.DataPatient;
import models.Patient;

public class PatientSelectionListener implements ChangeListener<Patient> {

    @SuppressWarnings("unused")
	private DataPatient dp;
    private RootCtrl rc;

    public PatientSelectionListener(RootCtrl rootCtrl, DataPatient dp) {
        rc = rootCtrl;
        this.dp = dp;
    }

    @Override
    public void changed(ObservableValue<? extends Patient> observable, Patient oldValue, Patient newValue) {
        if(newValue != null) {
            rc.getpSelected().setText(newValue.getFirstName());
            newValue.setEmail("modifUi@fred.fred");
            //dp.saveEntity(newValue);
        }else{
            rc.getpSelected().setText("");
        }
        if(oldValue != null) {
            //dp.saveEntity(oldValue);
        }
    }
}
