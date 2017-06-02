package metier.hibernate.data;

import java.util.List;

import metier.hibernate.Data;
import metier.hibernate.data.exceptions.DbDeleteException;
import metier.hibernate.data.exceptions.DbGetException;
import models.Rdv;

public class DataRdv extends Data<Rdv> {
	public DataRdv(){super();}
	
	public List<Rdv> getAll() throws DbGetException
    {
        return session.createQuery("from Rdv", Rdv.class).getResultList();
    }
	
	@Override
    public void delete(Rdv rdv)throws DbDeleteException {
        if(session.find(Rdv.class, rdv.getId()) != null){
            super.delete(rdv);
        }else{
            throw new DbDeleteException("Element inconnu");
        }
    }
}
