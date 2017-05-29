package metier.interfaces;

import models.Medecin;
import models.compta.ComptaJournaliere;
import models.compta.ComptaMensuelle;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

public interface ICompta {

    // cree l'object pour l'afficher.
    ComptaMensuelle createComptaMensuelle(Month month, Year year, Medecin medecin);
    void calculComptaMensuelle(ComptaMensuelle comptaMensuelle);

    // cree l'object et l'ajoute a la base.
    void createComptaJournaliere(LocalDate date, Medecin medecin, float retrait);
    void calculComptaJournaliere(ComptaJournaliere comptaJournaliere);
}
