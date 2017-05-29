package models.compta;

import models.Medecin;
import models.Paiement;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ComptaJournaliere")
public class ComptaJournaliere extends Compta{

    protected long id;
    private LocalDate date;
    private float soldePrecedent;
    private List<Paiement> paiementList;

    public ComptaJournaliere(){}

    public ComptaJournaliere(Medecin medecin, LocalDate date) {
        super(medecin);
        this.date = date;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public Medecin getMedecin() {
        return medecin;
    }
    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    @Id
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Basic
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    @OneToMany(cascade = CascadeType.ALL)
    private List<Paiement> getPaiementList() {
        return paiementList;
    }
    private void setPaiementList(List<Paiement> paiementList) {
        this.paiementList = paiementList;
    }

    @Basic
    public float getSoldePrecedent() {
        return soldePrecedent;
    }
    public void setSoldePrecedent(float soldePrecedent) {
        this.soldePrecedent = soldePrecedent;
    }

    @Basic
    public int getNbCS() {
        return nbCS;
    }
    public void setNbCS(int nbCS) {
        this.nbCS = nbCS;
    }

    @Basic
    public int getNbC2() {
        return nbC2;
    }
    public void setNbC2(int nbC2) {
        this.nbC2 = nbC2;
    }

    @Basic
    public int getNbDIU() {
        return nbDIU;
    }
    public void setNbDIU(int nbDIU) {
        this.nbDIU = nbDIU;
    }

    @Basic
    public int getNbEcho() {
        return nbEcho;
    }
    public void setNbEcho(int nbEcho) {
        this.nbEcho = nbEcho;
    }

    @Basic
    public float getTp() {
        return Tp;
    }
    public void setTp(float Tp) {
        this.Tp = Tp;
    }

    @Basic
    public float getImpayer() {
        return impayer;
    }
    public void setImpayer(float impayer) {
        this.impayer = impayer;
    }

    @Basic
    public float getCheque() {
        return cheque;
    }
    public void setCheque(float cheque) {
        this.cheque = cheque;
    }

    @Basic
    public float getEspece() {
        return espece;
    }
    public void setEspece(float espece) {
        this.espece = espece;
    }

    @Basic
    public float getCB() {
        return CB;
    }
    public void setCB(float CB) {
        this.CB = CB;
    }

    @Basic
    public float getRetrait() {
        return retrait;
    }
    public void setRetrait(float retrait) {
        this.retrait = retrait;
    }
}
