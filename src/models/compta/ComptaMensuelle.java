package models.compta;

import models.Medecin;

import java.time.YearMonth;
import java.util.List;

public class ComptaMensuelle extends Compta {

    private YearMonth date;
    private List<ComptaJournaliere> comptaJournaliere;
    private float solde;

    public ComptaMensuelle(Medecin medecin, YearMonth date) {
        super(medecin);
        this.date = date;
    }

    public YearMonth getDate() {
        return date;
    }

    public void setDate(YearMonth date) {
        this.date = date;
    }

    public List<ComptaJournaliere> getComptaJournaliere() {
        return comptaJournaliere;
    }

    public void setComptaJournaliere(List<ComptaJournaliere> comptaJournaliere) {
        this.comptaJournaliere = comptaJournaliere;
    }

    public void setSolde() {
        solde = espece - retrait;
    }

    public float getsolde() {
        return solde;
    }
}
