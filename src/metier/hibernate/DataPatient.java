package metier.hibernate;

import metier.hibernate.interfaces.IDataGetAll;
import models.Patient;

import java.util.List;

public class DataPatient extends Data<Patient> implements IDataGetAll<Patient>{

    public DataPatient(){
        super();
    }

    @Override
    public boolean deleteEntity(Patient p) {
        try {
            if(session.find(Patient.class, p.getId()) != null) {
                return super.deleteEntity(p);
            }
        }catch (Exception ignored){}
        return false;
    }

    @Override
    public List<Patient> getEntities() {
        return session.createQuery("from Patient ", Patient.class).getResultList();
    }
}
