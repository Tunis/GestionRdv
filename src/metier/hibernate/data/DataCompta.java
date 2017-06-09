package metier.hibernate.data;

import metier.hibernate.Data;
import metier.hibernate.data.exceptions.DbGetException;
import metier.hibernate.data.interfaces.IDbCompta;
import models.Medecin;
import models.Rdv;
import models.compta.ComptaJPaiement;
import models.compta.ComptaJournaliere;

import java.time.LocalDate;
import java.time.Month;
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
                .setParameter("month", month.getValue())
                .setParameter("year", year)
                .getResultList();
    }

    @Override
    public ComptaJournaliere getComptaOfDay(Medecin medecin, LocalDate date) throws DbGetException {
        ComptaJournaliere cj;
        try {
            cj = session.createQuery("from ComptaJournaliere where " +
                    "medecin = :medecin and " +
                    "date = :date", ComptaJournaliere.class)
                    .setParameter("medecin", medecin)
                    .setParameter("date", date)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DbGetException("entity inconnu");
        }
        return cj;
    }

    public List<Rdv> getPaiementUntil(Medecin medecin, LocalDate to, LocalDate from) {
        return session.createQuery("from Rdv r WHERE r.presentDay.medecin = :medecin AND ((r.paiement.date <= :to AND r.paiement.date > :from) OR (r.paiement.date is null AND r.presentDay.present = :to ))", Rdv.class)
                .setParameter("medecin", medecin)
                .setParameter("to", to)
                .setParameter("from", from)
                .getResultList();
    }

    public void saveComptaJPaiement(List<ComptaJPaiement> paiementList) {
        for (ComptaJPaiement comptaJPaiement : paiementList) {
            session.beginTransaction();
            session.save(comptaJPaiement);
            session.getTransaction().commit();
        }
    }
}