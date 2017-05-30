package metier.action;

import app.controller.test.Metier;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import metier.hibernate.data.DataPatient;
import metier.hibernate.data.exceptions.*;
import models.Adresse;
import models.Patient;

import java.time.LocalDate;
import java.util.function.Predicate;

public class MPatient extends Metier<Patient>{




	/*
	createPatient
	savePatient <- dans
	deletePatient
	getAllPatient

	list de tous les patient, a la creation ajout a la liste, a la suppression on l'enleve de la liste
	 */

	//private ListProperty<Patient> patients;
	private DataPatient db;

	public MPatient(){
		db = new DataPatient();
		try {
			list = new SimpleListProperty<>(FXCollections.observableArrayList(db.getAll()));
			list.sort(Patient::compareTo);
		}catch (DbGetException e){
			e.printStackTrace();
		}
	}

	public void createPatient(String nom, String prenom, String nomJf, String mail, int numSecu, String tel, LocalDate date, Adresse addr) throws DbDuplicateException, DbCreateException{
		// TODO: 25/05/2017 changer signature avec les bon parametre.
		// creation du test pour verifier si le patient est deja existant?
		Predicate<Patient> createPredi = p -> p.getMaidenName().equals(nomJf) && p.getBornDate().equals(date) && p.getFirstName().equals(prenom);


		if(list.stream().anyMatch(createPredi)) {
			// patient deja connu a priori?
			throw new DbDuplicateException();
		}else{
			// on cree le nouveau patient
			Patient patient = new Patient(nom, prenom, nomJf, tel, mail, null, numSecu, date, addr);
			// on tente de le saver dans la bdd, throw createException
			try {
				db.save(patient);
			}catch (Exception e){
				throw new DbCreateException();
			}
				// patient sauvé en bdd on l'ajoute a la liste des patients.
				// moyen de mettre a jour l'ui?
			list.get().add(patient);
			list.sort(Patient::compareTo);

		}
	}

	// a apeller a chaque changement de patient/ maj d'un patient etc...
	public Patient getPatient(Patient p) throws DbGetException {
		return db.getPatient(p);
	}

	public void save(Patient p) throws DbSaveException {
		db.save(p);
	}

	public void delete(Patient p) throws DbDeleteException{
		db.delete(p);
		list.remove(p);
	}
}
