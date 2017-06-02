package metier.action;

import app.controller.test.Metier;
import javafx.collections.FXCollections;
import metier.hibernate.data.DataPaiement;
import metier.hibernate.data.exceptions.DbSaveException;
import models.*;

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
        list.set(FXCollections.observableArrayList(db.getAllPaiementImpayer()));
        list.sort(Paiement::compareTo);
    }

	public Paiement createPaiement(float espece, Cheque cheque, float cb, Tp tp, float prix, boolean payer, LocalDate date, Medecin medecin, Rdv rdv) throws DbSaveException {
        Paiement p = new Paiement(espece, cheque, cb, tp, prix, payer, date, medecin, rdv);
        db.save(p);
        if(!p.isPayer())
            list.add(p);
        return p;
    }

    public void editPaiement(Paiement p) throws DbSaveException {
        db.save(p);
        if(p.isPayer())
            list.remove(p);
    }

    public List<Paiement> getPaiementOfDay(Medecin medecin, LocalDate date){
        return db.getAllPaiementOfDay(medecin, date);
    }


}
