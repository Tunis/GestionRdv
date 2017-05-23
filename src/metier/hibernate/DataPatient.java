package metier.hibernate;

import metier.hibernate.interfaces.DataGettersSetters;
import models.Patient;

public class DataPatient extends Data<Patient> implements DataGettersSetters<Patient>{

    public DataPatient(){
        super();
        entities.setAll(session.createQuery("from Patient ").getResultList());
    }

    @Override
    public Patient getEntity(long id) {
        try {
            return session.find(Patient.class, id);
        }catch (Exception ignored){
            return null;
        }
    }

    @Override
    public boolean deleteEntity(Patient p) {
        try {
            if(session.find(Patient.class, p.getId()) != null) {
                session.remove(p);
            }
            return true;
        }catch (Exception ignored){
            return false;
        }
    }
}
