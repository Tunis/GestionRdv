package metier.hibernate;

import javafx.collections.FXCollections;
import models.*;

import java.time.LocalDate;
import java.util.List;

public class DataCompta extends Data<ComptaJournaliere> {

    public DataCompta() {super();}

    public List<Paiement> getPaiementDay(LocalDate date, Medecin medecin) {
        return session.createQuery("from Paiement where " +
                                    "Medecin = :medecin AND date = :date",
                                    Paiement.class)
                .setParameter("medecin", medecin)
                .setParameter("date", date)
                .getResultList();
    }

    public ComptaJournaliere getComptaDay(LocalDate date, Medecin medecin) {
        return session
                .createQuery("from ComptaJournaliere where " +
                            "date = :date AND medecin = :medecin",
                            ComptaJournaliere.class)
                .setParameter("date", date)
                .setParameter("medecin", medecin)
                .getSingleResult();
    }

    public void setComptaMensuelle(LocalDate date, Medecin medecin){
        int monthValue = date.getMonthValue();
        int year = date.getYear();
        List<ComptaJournaliere> list = session
                .createQuery("from ComptaJournaliere WHERE " +
                            "MONTH(:month) and YEAR(:year) and medecin = :medecin",
                            ComptaJournaliere.class)
                .setParameter("month", monthValue)
                .setParameter("year", year)
                .setParameter("medecin", medecin)
                .getResultList();
        entities = FXCollections.observableArrayList(list);
    }
}
