package metier.action;

import metier.hibernate.data.interfaces.IDbCompta;
import models.Medecin;
import models.compta.ComptaJournaliere;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;

public class MCompta implements IDbCompta{
// TODO: 25/05/2017 a faire

	@Override
	public boolean save(ComptaJournaliere entity) {
		return false;
	}

	@Override
	public List<ComptaJournaliere> getComptaOfMonth(Medecin medecin, Month month, Year year) {
		return null;
	}

	@Override
	public ComptaJournaliere getComptaOfDay(Medecin medecin, LocalDate date) {
		return null;
	}

	/*
	creer compta journaliere
	calculer compta journaliere

	// quand creation de compta journaliere on recreer la compta du mois.

	creer compta mensuelle
	calculer compta mensuelle
	 */
}
