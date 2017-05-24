package metier.hibernate;

import metier.hibernate.interfaces.IDataGetAll;
import models.Medecin;

import java.util.List;

public class DataMedecin extends Data<Medecin> implements IDataGetAll<Medecin>{

    public DataMedecin(){
        super();
    }

    @Override
    public boolean deleteEntity(Medecin e) {
        try {
            if(session.find(Medecin.class, e.getId()) != null) {
                return super.deleteEntity(e);
            }
        }catch (Exception ignored){}
        return false;
    }

    @Override
    public List<Medecin> getEntities() {
        return session.createQuery("from Medecin", Medecin.class).getResultList();
    }
}
