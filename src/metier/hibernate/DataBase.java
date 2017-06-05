package metier.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.TimeZone;

public class DataBase {
    private static final SessionFactory sessionFactory;
	private static Session SESSION;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
	        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localHost:3306/test?serverTimezone=" + TimeZone.getDefault().getID());

            sessionFactory = configuration.buildSessionFactory();
	        SESSION = sessionFactory.openSession();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
	    if (!SESSION.isConnected())
		    SESSION = sessionFactory.openSession();
	    return SESSION;

    }

    public static void close(){
        sessionFactory.close();
    }
}
