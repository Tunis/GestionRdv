package models;

import metier.hibernate.DataCompta;

import java.time.LocalDate;


public abstract class Compta {

    protected DataCompta dc;

    protected Medecin medecin;

    protected LocalDate date;
    protected short nbCS;
    protected short nbC2;
    protected short nbDIU;
    protected short nbEcho;
    protected short nbTp;
    protected float impayer;
    protected float cheque;
    protected float espece;
    protected float CB;
    protected float retrait;

    public Compta() {}

    public Compta(DataCompta dc) {
        this.dc = dc;
    }

    public Compta(DataCompta dc, Medecin medecin, LocalDate date) {
        this(dc);
        this.medecin = medecin;
        this.date = date;
    }

    protected abstract void calculCompta();
}
