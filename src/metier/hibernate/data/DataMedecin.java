package metier.hibernate.data;

import metier.hibernate.Data;
import metier.hibernate.data.exceptions.DbDeleteException;
import metier.hibernate.data.interfaces.IDbPersons;
import models.Medecin;
import models.Tp;

import java.time.YearMonth;
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
        if(session.find(Medecin.class, entity.getId()) != null){
            super.delete(entity);
        }else {
            throw new DbDeleteException();
        }
    }

    public Medecin get(Medecin m) {
        return session.get(Medecin.class, m.getId());
    }

	public List<Tp> getTp(Medecin m, YearMonth month) {
		return session.createQuery("from Tp tp where tp.paiement.medecin = :medecin and MONTH(tp.paiement.rdv.presentDay.present) = :month and YEAR(tp.paiement.rdv.presentDay.present) = :year", Tp.class)
				.setParameter("medecin", m)
				.setParameter("month", month.getMonthValue())
				.setParameter("year", month.getYear())
				.getResultList();
	}

	public void saveTp(Tp newValue) {
		try {
			session.beginTransaction();
			session.save(newValue);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
