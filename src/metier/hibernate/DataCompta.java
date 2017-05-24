package metier.hibernate;

import models.Medecin;
import models.compta.ComptaJournaliere;

import java.time.LocalDate;
import java.util.List;

public class DataCompta extends Data<ComptaJournaliere> {

    public DataCompta() {super();}

    public List<ComptaJournaliere> getEntities(Medecin medecin, LocalDate date) {
        return session.createQuery("from ComptaJournaliere where " +
                "medecin = :medecin and " +
                "MONTH(date) = :month and " +
                "YEAR(date) = :year", ComptaJournaliere.class)
                .setParameter("medecin", medecin)
                .setParameter("month", date.getMonth())
                .setParameter("year", date.getYear())
                .getResultList();
    }
}
