package models;

import metier.hibernate.DataCompta;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.List;

public class ComptaMensuelle extends Compta {

    private List<ComptaJournaliere> comptaJournaliere;

    public ComptaMensuelle(DataCompta dc, Medecin medecin, LocalDate date) {
        super(dc, medecin, date);
        dc.setComptaMensuelle(date, medecin);
        comptaJournaliere = dc.getEntities();
        calculCompta();
    }

    @Override
    protected void calculCompta() {
        for (ComptaJournaliere compta : comptaJournaliere) {
            nbC2 += compta.getNbC2();
            nbCS += compta.getNbCS();
            nbDIU += compta.getNbDIU();
            nbEcho += compta.getNbEcho();
            nbTp += compta.getNbTp();
            CB += compta.getCB();
            retrait += compta.getRetrait();
            espece += (compta.getEspece() - compta.getRetrait());
            cheque += compta.getCheque();
            impayer += compta.getImpayer();
        }
    }

    public List<ComptaJournaliere> getComptaJournaliere() {
        return comptaJournaliere;
    }

    public void setComptaJournaliere(List<ComptaJournaliere> comptaJournaliere) {
        this.comptaJournaliere = comptaJournaliere;
    }

    public void changeMedecin(Medecin m){
        medecin = m;
        dc.setComptaMensuelle(date, medecin);
        comptaJournaliere = dc.getEntities();
        calculCompta();
    }

    public void changeDate(LocalDate date){
        this.date = date;
        dc.setComptaMensuelle(date, medecin);
        comptaJournaliere = dc.getEntities();
        calculCompta();
    }
}
