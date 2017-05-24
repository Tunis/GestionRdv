package models.compta;

import models.Medecin;

import java.time.Month;
import java.time.Year;
import java.util.List;

public class ComptaMensuelle extends Compta {

    private Month month;
    private Year year;
    private List<ComptaJournaliere> comptaJournaliere;

    public ComptaMensuelle(Medecin medecin, Month month, Year year) {
        super(medecin);
        this.month = month;
        this.year = year;
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
