package metier.action;

import metier.hibernate.data.DataCompta;
import metier.hibernate.data.interfaces.IDbCompta;
import models.Medecin;
import models.PresentDay;
import models.Rdv;
import models.compta.ComptaJournaliere;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;

public class MCompta implements IDbCompta{
// TODO: 25/05/2017 a faire

	private DataCompta db;

	public MCompta(DataCompta db) {
		this.db = db;
	}

	@Override
	public void save(ComptaJournaliere entity) {}

	@Override
	public List<ComptaJournaliere> getComptaOfMonth(Medecin medecin, Month month, Year year) {
		return db.getComptaOfMonth(medecin,month,year);
	}

	@Override
	public ComptaJournaliere getComptaOfDay(Medecin medecin, LocalDate date) {
		return db.getComptaOfDay(medecin, date);
	}

	public void createComptaOfDay(Medecin medecin, LocalDate date) {
		List<Rdv> presentDay = medecin.getPlannings().stream().findFirst().filter(e -> e.getPresent().equals(date)).get().getRdvList();

	}
	/*
	creer compta journaliere
	calculer compta journaliere

	// quand creation de compta journaliere on recreer la compta du mois.

	creer compta mensuelle
	calculer compta mensuelle
	 */
}
