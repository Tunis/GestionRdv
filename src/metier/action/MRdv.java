package metier.action;

import javafx.collections.FXCollections;
import metier.hibernate.data.DataRdv;
import metier.hibernate.data.exceptions.*;
import models.Patient;
import models.PresentDay;
import models.Rdv;
import models.enums.TypeRdv;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Predicate;

public class MRdv extends Metier<Rdv>{

	private DataRdv db;

	public MRdv() {
		db = new DataRdv();
		try {
			setRdvOfDay(LocalDate.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			rdv.getPatient().getRdvList().add(rdv);
			// on tente de le saver dans la bdd, throw createException
			try {
				rdv.getPresentDay().getRdvList().add(rdv);
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

	// liste de tous les rdv du jour
	public void setRdvOfDay(LocalDate date) throws DbGetException {
		list.clear();
		listProperty().set(FXCollections.observableArrayList(db.getRdvOfDay(date)));
		list.sort(Rdv::compareTo);
	}

	public void save(Rdv rdv) throws DbSaveException {
		db.save(rdv);
	}

	public void delete(Rdv rdv) throws DbDeleteException{
		db.delete(rdv);
	}
}
