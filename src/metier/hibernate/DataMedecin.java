package metier.hibernate;

import metier.hibernate.interfaces.DataGettersSetters;
import models.Medecin;

public class DataMedecin extends Data<Medecin> implements DataGettersSetters<Medecin>{

    public DataMedecin(){
        super();
        entities.setAll(session.createQuery("from Medecin ").getResultList());
    }


    @Override
    public Medecin getEntity(long id) {
        try {
            return session.find(Medecin.class, id);
        }catch (Exception ignored){
            return null;
        }
    }

    @Override
    public boolean deleteEntity(Medecin e) {
        try {
            if(session.find(Medecin.class, e.getId()) != null) {
                session.remove(e);
            }
            return true;
        }catch (Exception ignored){
            return false;
        }
    }
}
