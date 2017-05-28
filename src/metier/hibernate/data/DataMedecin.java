package metier.hibernate.data;

import metier.hibernate.Data;
import metier.hibernate.data.exceptions.DbDeleteException;
import metier.hibernate.data.interfaces.IDbPersons;
import models.Medecin;

import java.util.List;

public class DataMedecin extends Data<Medecin> implements IDbPersons<Medecin>{

    public DataMedecin(){
        super();
    }

    @Override
    public List<Medecin> getAll() {
        return session.createQuery("from Medecin", Medecin.class).getResultList();
    }

    public void delete(Medecin entity) throws DbDeleteException {
        if(session.find(Medecin.class, entity) != null){
            super.delete(entity);
        }else {
            throw new DbDeleteException();
        }
    }

    public Medecin get(Medecin m) {
        return session.get(Medecin.class, m.getId());
    }
}
