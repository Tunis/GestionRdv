package metier.hibernate;

import org.hibernate.Session;

public abstract class Data<T> {

    protected Session session;

    protected Data(){
        session = DataBase.getSession();
    }

    public boolean saveEntity(T e){
        try {
            // TODO: 24/05/2017 verifier le retour de save si echoue
            session.beginTransaction();
            session.save(e);
            session.getTransaction().commit();

            return true;
        }catch (Exception ignored){
            return false;
        }
    }

    public boolean deleteEntity(T e){
        try {
            // TODO: 24/05/2017 verifier le retour de remove si echoue.
            session.remove(e);
            return true;
        }catch (Exception ignored){
            return false;
        }
    }
}
