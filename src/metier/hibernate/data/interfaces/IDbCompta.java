package metier.hibernate.data.interfaces;

import models.Medecin;
import models.compta.ComptaJournaliere;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;

public interface IDbCompta extends IDb<ComptaJournaliere> {

	List<ComptaJournaliere> getComptaOfMonth(Medecin medecin, Month month, Year year);
	ComptaJournaliere getComptaOfDay(Medecin medecin, LocalDate date);
}
