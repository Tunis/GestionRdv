package metier.hibernate.data.interfaces;

import metier.hibernate.data.exceptions.DbGetException;
import models.Medecin;
import models.compta.ComptaJournaliere;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public interface IDbCompta extends IDb<ComptaJournaliere> {

    ComptaJournaliere getComptaOfDay(Medecin medecin, LocalDate date) throws DbGetException;

    List<ComptaJournaliere> loadComptaOfMonth(Medecin medecin, Month month, int year);
}
