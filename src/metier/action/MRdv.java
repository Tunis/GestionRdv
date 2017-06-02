package metier.action;

import java.time.Duration;
import java.time.LocalTime;
import java.util.function.Predicate;

import app.controller.test.Metier;
import metier.hibernate.data.DataRdv;
import metier.hibernate.data.exceptions.DbCreateException;
import metier.hibernate.data.exceptions.DbDeleteException;
import metier.hibernate.data.exceptions.DbDuplicateException;
import metier.hibernate.data.exceptions.DbGetException;
import metier.hibernate.data.exceptions.DbSaveException;
import models.Patient;
import models.PresentDay;
import models.Rdv;
import models.TypeRdv;

public class MRdv extends Metier<Rdv>{

	private DataRdv db;

	public MRdv(){
		db = new DataRdv();
	}

	public Rdv createRdv(String cotation, Duration duration, TypeRdv typeRdv, LocalTime time, Patient patient, PresentDay presentDay) throws DbDuplicateException, DbCreateException{
		// TODO: 25/05/2017 changer signature avec les bon parametre.
		// creation du test pour verifier si le rdv est deja existant? (patient, medecin, date, time)
		Predicate<Rdv> createPredi = rdv -> rdv.getPatient().equals(patient) && rdv.getPresentDay().getMedecin().equals(presentDay.getMedecin()) && rdv.getPresentDay().getPresent().equals(presentDay.getPresent()) && rdv.getTime().equals(time);

		if(list.stream().anyMatch(createPredi)) {
			// rdv deja connu a priori?
			throw new DbDuplicateException();
		}else{
			// on cree le nouveau rdv
			Rdv rdv = new Rdv(cotation, duration, typeRdv, time, patient, presentDay);
			// on tente de le saver dans la bdd, throw createException
			try {
				db.save(rdv);
			}catch (Exception e){
				throw new DbCreateException();
			}
			
			return rdv;
		}
	}
	
	public void editRdv(Rdv rdv) throws DbCreateException{
		try {
			db.save(rdv);
		}catch (Exception e){
			throw new DbCreateException();
		}
	}
	
	// a apeller a chaque changement de patient/ maj d'un patient etc...
	public Rdv getRdv(Rdv rdv) throws DbGetException {
		return null;
		//return db.getPatient(p);
	}

	public void save(Rdv rdv) throws DbSaveException {
		db.save(rdv);
	}

	public void delete(Rdv rdv) throws DbDeleteException{
		db.delete(rdv);;
	}
}
