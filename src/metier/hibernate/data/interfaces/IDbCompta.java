package metier.hibernate.data.interfaces;

import models.Medecin;
import models.compta.ComptaJournaliere;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public interface IDbCompta extends IDb<ComptaJournaliere> {

	ComptaJournaliere getComptaOfDay(Medecin medecin, LocalDate date);
	List<ComptaJournaliere> loadComptaOfMonth(Medecin medecin, Month month, int year);
}
