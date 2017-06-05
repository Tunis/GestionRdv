package metier.hibernate;

import metier.hibernate.data.exceptions.DbDeleteException;
import metier.hibernate.data.exceptions.DbSaveException;
import metier.hibernate.data.interfaces.IDb;
import org.hibernate.Session;

public abstract class Data<T> implements IDb<T> {

    protected Session session;

    protected Data(){
        session = DataBase.getSession();
    }

    @Override
    public void save(T e) throws DbSaveException{
        // TODO: 24/05/2017 verifier le retour de save si echoue
        try {
            session.beginTransaction();
	        session.saveOrUpdate(e);
	        session.getTransaction().commit();
        }catch (Exception ex){
        	ex.printStackTrace();
            //throw new DbSaveException("erreur sql");
        }
    }

    protected void delete(T e) throws DbDeleteException {
        // TODO: 24/05/2017 verifier le retour de remove si echoue.
        try {
            session.beginTransaction();
            session.delete(e);
            session.getTransaction().commit();
        }catch (Exception ex){
            throw new DbDeleteException("erreur sql");
        }
    }
}
