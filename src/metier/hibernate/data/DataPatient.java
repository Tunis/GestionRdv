package metier.hibernate.data;

import metier.hibernate.Data;
import metier.hibernate.data.exceptions.DbGetException;
import metier.hibernate.data.interfaces.IDbPersons;
import models.Patient;

import java.util.List;

public class DataPatient extends Data<Patient> implements IDbPersons<Patient> {

    public DataPatient(){
        super();
    }

    @Override
    public List<Patient> getAll() throws DbGetException
    {
        return session.createQuery("from Patient p", Patient.class).getResultList();
    }

    @Override
    public boolean delete(Patient patient){
        if(session.find(Patient.class, patient) != null){
            return super.delete(patient);
        }
        return false;
    }

    public Patient getPatient(Patient p) throws DbGetException{
        try {
            return session.find(Patient.class, p);
        }catch (Exception e){
            throw new DbGetException();
        }
    }
}
