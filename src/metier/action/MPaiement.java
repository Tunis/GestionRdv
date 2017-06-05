package metier.action;

import javafx.collections.FXCollections;
import metier.hibernate.data.DataPaiement;
import metier.hibernate.data.exceptions.DbSaveException;
import models.Medecin;
import models.Paiement;

import java.time.LocalDate;
import java.util.List;

public class MPaiement extends Metier<Paiement> {

	/*
	createPaiement
	editPaiement
	getAllPaiementImpayer -> mis a jour au debut puis a chaque modification d'un paiement
	getAllPaiementDuJour

	liste Paiement impayer, a la creation on verifi si payer si non on l'ajoute a la liste des impayer,
	a l'edit, on enleve le precedent de la liste des impayer et on cree un nouveau a la nouvelle date.
	 */
    private DataPaiement db;

    public MPaiement(){
        this.db = new DataPaiement();
	    setPaiementImpayer();
    }

    public List<Paiement> getPaiementOfDay(Medecin medecin, LocalDate date){
        return db.getAllPaiementOfDay(medecin, date);
    }

	public void setPaiementImpayer() {
		listProperty().clear();
		listProperty().set(FXCollections.observableArrayList(db.getAllPaiementImpayer()));
		listProperty().sort(Paiement::compareTo);
	}


	public void remove(Paiement newValue) {
		list.remove(newValue);
	}

	public void save(Paiement newValue) throws DbSaveException {
		db.save(newValue);
	}
}
