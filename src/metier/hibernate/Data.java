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
        session.beginTransaction();
        session.save(e);
        session.getTransaction().commit();
    }

    protected void delete(T e) throws DbDeleteException {
        // TODO: 24/05/2017 verifier le retour de remove si echoue.
        session.remove(e);
    }
}
