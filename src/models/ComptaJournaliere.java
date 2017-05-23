package models;

import metier.hibernate.DataCompta;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ComptaJournaliere")
public class ComptaJournaliere extends Compta{

    protected long id;
    private float soldePrecedent;

    public ComptaJournaliere(){}

    public ComptaJournaliere(DataCompta dc, Medecin medecin, LocalDate date) {
        super(dc, medecin, date);
        calculCompta();
    }

    @Override
    protected void calculCompta() {
        List<Paiement> paiementDay = dc.getPaiementDay(date, medecin);
        for (Paiement paiement : paiementDay) {
            nbC2 += paiement.getRdv().getTypeRdv().equals(TypeRdv.C2) ? 1 : 0;
            nbCS += paiement.getRdv().getTypeRdv().equals(TypeRdv.CS) ? 1 : 0;
            nbDIU += paiement.getRdv().getTypeRdv().equals(TypeRdv.DUI) ? 1 : 0;
            nbEcho += paiement.getRdv().getTypeRdv().equals(TypeRdv.ECHO) ? 1 : 0;
            nbTp += paiement.getTp() != null ? paiement.getTp().getMontant() : 0;
            CB += paiement.getCb();
            espece = paiement.getEspece();
            cheque = paiement.getCheque().getMontant();
            impayer = paiement.getPrix() - (CB + espece + cheque);
            if(date.getDayOfMonth() > 1){
                dc.getComptaDay(date.minusDays(1), medecin);
            }else
                soldePrecedent = 0;

            dc.saveEntity(this);
        }
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

    @Basic
    public short getNbCS() {
        return nbCS;
    }

    public void setNbCS(short nbCS) {
        this.nbCS = nbCS;
    }

    @Basic
    public short getNbC2() {
        return nbC2;
    }

    public void setNbC2(short nbC2) {
        this.nbC2 = nbC2;
    }

    @Basic
    public short getNbDIU() {
        return nbDIU;
    }

    public void setNbDIU(short nbDIU) {
        this.nbDIU = nbDIU;
    }

    @Basic
    public short getNbEcho() {
        return nbEcho;
    }

    public void setNbEcho(short nbEcho) {
        this.nbEcho = nbEcho;
    }

    @Basic
    public short getNbTp() {
        return nbTp;
    }

    public void setNbTp(short nbTp) {
        this.nbTp = nbTp;
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
    public float getSoldePrecedent() {
        return soldePrecedent;
    }

    public void setSoldePrecedent(float soldePrecedent) {
        this.soldePrecedent = soldePrecedent;
    }

    @Basic
    public float getRetrait() {
        return retrait;
    }

    public void setRetrait(float retrait) {
        this.retrait = retrait;
    }

    private void addRetrait(float retrait){this.retrait += retrait;}

    private void modifRetrait(float retrait){
        addRetrait(retrait);
        soldePrecedent -= retrait;
        dc.saveEntity(this);
    }
}
