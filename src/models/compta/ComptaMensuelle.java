package models.compta;

import models.Medecin;

import java.time.Month;
import java.time.Year;
import java.util.List;

public class ComptaMensuelle extends Compta {

    private Month month;
    private int year;
    private List<ComptaJournaliere> comptaJournaliere;

    public ComptaMensuelle(Medecin medecin, Month month, int year) {
        super(medecin);
        this.month = month;
        this.year = year;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<ComptaJournaliere> getComptaJournaliere() {
        return comptaJournaliere;
    }

    public void setComptaJournaliere(List<ComptaJournaliere> comptaJournaliere) {
        this.comptaJournaliere = comptaJournaliere;
    }

    protected void calculCompta() {
        for (ComptaJournaliere compta : comptaJournaliere) {
            nbC2 += compta.getNbC2();
            nbCS += compta.getNbCS();
            nbDIU += compta.getNbDIU();
            nbEcho += compta.getNbEcho();
            Tp += compta.getTp();
            CB += compta.getCB();
            retrait += compta.getRetrait();
            espece += (compta.getEspece() - compta.getRetrait());
            cheque += compta.getCheque();
            impayer += compta.getImpayer();
        }
        espece -= retrait;
    }



}
