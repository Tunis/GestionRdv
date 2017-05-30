package models.compta;


import models.Medecin;

public abstract class Compta {

    protected Medecin medecin;

    protected int nbCS;
    protected int nbC2;
    protected int nbDIU;
    protected int nbEcho;
    protected float Tp;
    protected float impayer;
    protected float cheque;
    protected float espece;
    protected float CB;
    protected float retrait;

    protected int nbTp;
    protected int nbCB;
    protected int nbEspece;
    protected int nbCheque;

    public Compta() {}

    public Compta(Medecin medecin) {
        this.medecin = medecin;
    }

    public int getNbCS() {
        return nbCS;
    }
    public void setNbCS(int nbCS) {
        this.nbCS = nbCS;
    }
    public void addCS(int nb) {
        nbCS += nb;
    }

    public int getNbC2() {
        return nbC2;
    }
    public void setNbC2(int nbC2) {
        this.nbC2 = nbC2;
    }
    public void addC2(int nb) {
        nbC2 += nb;
    }

    public int getNbDIU() {
        return nbDIU;
    }
    public void setNbDIU(int nbDIU) {
        this.nbDIU = nbDIU;
    }
    public void addDIU(int nb) {
        nbDIU += nb;
    }

    public int getNbEcho() {
        return nbEcho;
    }
    public void setNbEcho(int nbEcho) {
        this.nbEcho = nbEcho;
    }
    public void addEcho(int nb) {
        nbEcho += nb;
    }

    public float getTp() {
        return Tp;
    }
    public void setTp(float Tp) {
        this.Tp = Tp;
    }
    public void addTp(float tp) {
        this.Tp += tp;
    }

    public float getImpayer() {
        return impayer;
    }
    public void setImpayer(float impayer) {
        this.impayer = impayer;
    }
    public void addImpayer(float impayer) {
        this.impayer += impayer;
    }

    public float getCheque() {
        return cheque;
    }
    public void setCheque(float cheque) {
        this.cheque = cheque;
    }
    public void addCheque(float cheque) {
        this.cheque += cheque;
    }

    public float getEspece() {
        return espece;
    }
    public void setEspece(float espece) {
        this.espece = espece;
    }
    public void addEspece(float espece) {
        this.espece += espece;
    }

    public float getCB() {
        return CB;
    }
    public void setCB(float CB) {
        this.CB = CB;
    }
    public void addCB(float cb) {
        this.CB += cb;
    }

    public float getRetrait() {
        return retrait;
    }
    public void setRetrait(float retrait) {
        this.retrait = retrait;
    }
    public void addRetrait(float retrait) {
        this.retrait += retrait;
    }

    public int getNbTp() {
        return nbTp;
    }
    public void setNbTp(int nbTp) {
        this.nbTp = nbTp;
    }
    public void addNbTp(int nb){nbTp += nb;}

    public int getNbCB() {
        return nbCB;
    }
    public void setNbCB(int nbCB) {
        this.nbCB = nbCB;
    }
    public void addNbCB(int nb){nbCB += nb;}

    public int getNbEspece() {
        return nbEspece;
    }
    public void setNbEspece(int nbEspece) {
        this.nbEspece = nbEspece;
    }
    public void addNbEspece(int nb){nbEspece += nb;}

    public int getNbCheque() {
        return nbCheque;
    }
    public void setNbCheque(int nbCheque) {
        this.nbCheque = nbCheque;
    }
    public void addNbCheque(int nb){nbCheque += nb;}
}
