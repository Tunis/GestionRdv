package metier.action;

import app.controller.test.Metier;
import javafx.collections.FXCollections;
import metier.hibernate.data.DataMedecin;
import metier.hibernate.data.exceptions.*;
import models.Medecin;

import java.util.function.Predicate;

public class MMedecin extends Metier<Medecin> {

	/*
	getAllMedecin
	createMedecin
	saveMedecin
	deleteMedecin

	liste de tous les medecin comme pour les Medecin.
	 */

	private DataMedecin db;

	public MMedecin(){
		this.db = new DataMedecin();
		list.set(FXCollections.observableArrayList(db.getAll()));
		list.sort(Medecin::compareTo);
	}

	public void createMedecin(String nom, String prenom, String mail, String tel) throws DbDuplicateException, DbCreateException {
		// TODO: 25/05/2017 changer signature avec les bon parametre.
		// creation du test pour verifier si le Medecin est deja existant?
		Predicate<Medecin> createPredi = p -> p.getFirstName().equals(prenom) && p.getLastName().equals(nom);


		if (list.stream().anyMatch(createPredi)) {
			// Medecin deja connu a priori?
			throw new DbDuplicateException();
		} else {
			// on cree le nouveau Medecin
			Medecin medecin = new Medecin(nom, prenom, tel, mail);
			// on tente de le saver dans la bdd, throw createException
			try {
				db.save(medecin);
			}catch (Exception e){
				e.printStackTrace();
				//throw new DbCreateException();
			}
			// Medecin sauvé en bdd on l'ajoute a la liste des patients.
			// moyen de mettre a jour l'ui?
			list.get().add(medecin);
			list.sort(Medecin::compareTo);

		}
	}

	// a apeller a chaque changement de Medecin/ maj d'un Medecin etc...
	public Medecin get(Medecin m) throws DbGetException {
		return db.get(m);
	}

	public void save(Medecin m) throws DbSaveException {
		db.save(m);
	}

	public void delete(Medecin m) throws DbDeleteException {
		db.delete(m);
		list.remove(m);
	}
}
