package metier.action;

import metier.interfaces.IDbCompta;
import models.Medecin;
import models.compta.ComptaJournaliere;

import java.time.Month;
import java.util.List;

public class MCompta implements IDbCompta{


	@Override
	public boolean save(ComptaJournaliere entity) {
		return false;
	}

	@Override
	public List<ComptaJournaliere> getComptaOfMonth(Medecin medecin, Month month) {
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
