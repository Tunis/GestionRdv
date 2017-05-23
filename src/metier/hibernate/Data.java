package metier.hibernate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;

public abstract class Data<T> {

    protected Session session;
    protected ObservableList<T> entities = FXCollections.observableArrayList();

    protected Data(){
        session = DataBase.getSession();
    }

    public boolean saveEntity(T e){
        try {
            session.beginTransaction();
            session.save(e);
            session.getTransaction().commit();

            return true;
        }catch (Exception ignored){
            return false;
        }
    }

    public ObservableList<T> getEntities(){return entities;}

    public boolean createEntity(T e){
        try {
            entities.add(e);
            return saveEntity(e);
        }catch (Exception ignored){
            return false;
        }
    }
}
