package metier.interfaces;

import models.Medecin;
import models.compta.ComptaJournaliere;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public interface IDbCompta extends IDb<ComptaJournaliere> {

	List<ComptaJournaliere> getComptaOfMonth(Medecin medecin, Month month);


	ComptaJournaliere getComptaOfDay(Medecin medecin, LocalDate date);
}
