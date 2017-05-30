package metier.action;

import metier.hibernate.data.DataCompta;
import metier.hibernate.data.exceptions.DbSaveException;
import models.Medecin;
import models.PresentDay;
import models.Rdv;
import models.compta.Compta;
import models.compta.ComptaJournaliere;
import models.compta.ComptaMensuelle;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MCompta{
// TODO: 25/05/2017 a faire

	private DataCompta db;

	public MCompta(DataCompta db) {
		this.db = db;
	}

	public void save(ComptaJournaliere entity) {}

	public ComptaJournaliere getComptaOfDay(Medecin medecin, LocalDate date) {
		return db.getComptaOfDay(medecin, date);
	}

	public void createComptaOfDay(Medecin medecin, LocalDate date, float retrait) throws DbSaveException {
		Optional<PresentDay> first = medecin.getPlannings().stream().filter(e -> e.getPresent().equals(date)).findFirst();
		List<Rdv> rdvs = new ArrayList<>();
		if(first.isPresent())
			rdvs = first.get().getRdvList();

		ComptaJournaliere cj = new ComptaJournaliere(medecin, date, retrait);

		rdvs.forEach(r->{
			cj.addTypeRdv(r.getTypeRdv());

			cj.addEspece(r.getPaiement().getEspece());
			cj.addCB(r.getPaiement().getCb());
			cj.addCheque(r.getPaiement().getCheque().getMontant());
			cj.addTp(r.getPaiement().getTp().getMontant());
			cj.addImpayer(r.getPaiement().isPayer()? 0 : r.getPaiement().getPrix());

			if(r.getPaiement().getCb() != 0) cj.addNbCB(1);
			if(r.getPaiement().getCheque() != null) cj.addNbCheque(1);
			if(r.getPaiement().getEspece() != 0) cj.addNbEspece(1);
			if(r.getPaiement().getTp() != null) cj.addNbTp(1);

			cj.getPaiementList().add(r.getPaiement());
		});

		db.save(cj);
	}

	public ComptaMensuelle loadComptaOfMonth(Medecin medecin, LocalDate date){
		List<ComptaJournaliere> content = db.loadComptaOfMonth(medecin, date.getMonth(), date.getYear());
		ComptaMensuelle cm = new ComptaMensuelle(medecin, date.getMonth(), date.getYear());
		cm.setComptaJournaliere(content);

		float soldePrecedent = 0f;
		for (ComptaJournaliere cj : cm.getComptaJournaliere()) {
			cm.addCS(cj.getNbCS());
			cm.addC2(cj.getNbC2());
			cm.addEcho(cj.getNbEcho());
			cm.addDIU(cj.getNbDIU());

			cm.addImpayer(cj.getImpayer());
			cm.addCheque(cj.getCheque());
			cm.addCB(cj.getCB());
			cm.addEspece(cj.getEspece()-cj.getRetrait());
			cm.addTp(cj.getTp());
			cm.addRetrait(cj.getRetrait());

			cm.addNbCB(cj.getNbCB());
			cm.addNbCheque(cj.getNbCheque());
			cm.addNbEspece(cj.getNbEspece());
			cm.addNbTp(cj.getNbTp());

			cj.setSoldePrecedent(soldePrecedent);
			soldePrecedent = cj.getEspece() - cj.getRetrait();
		}
		return cm;
	}

	public void print(Compta compta){
		// TODO: 29/05/2017 a faire
	}
	/*
	creer compta journaliere
	calculer compta journaliere

	// quand creation de compta journaliere on recreer la compta du mois.

	creer compta mensuelle
	calculer compta mensuelle
	 */
}
