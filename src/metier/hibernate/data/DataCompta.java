package metier.hibernate.data;

import metier.hibernate.Data;
import metier.hibernate.data.interfaces.IDbCompta;
import models.Medecin;
import models.compta.ComptaJournaliere;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;

public class DataCompta extends Data<ComptaJournaliere> implements IDbCompta {

    public DataCompta() {super();}

    @Override
    public List<ComptaJournaliere> loadComptaOfMonth(Medecin medecin, Month month, int year) {
        return session.createQuery("from ComptaJournaliere where " +
                "medecin = :medecin and " +
                "MONTH(date) = :month and " +
                "YEAR(date) = :year ORDER BY date ASC", ComptaJournaliere.class)
                .setParameter("medecin", medecin)
                .setParameter("month", month)
                .setParameter("year", year)
                .getResultList();
    }

    @Override
    public ComptaJournaliere getComptaOfDay(Medecin medecin, LocalDate date) {
        return session.createQuery("from ComptaJournaliere where " +
                "medecin = :medecin and " +
                "date = :date", ComptaJournaliere.class)
                .setParameter("medecin", medecin)
                .setParameter("date", date)
                .getSingleResult();
    }
}
