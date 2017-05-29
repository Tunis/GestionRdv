package models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Paiement")
public class Paiement {

    private long id;
    private float espece;
    private Cheque cheque;
    private float cb;
    private Tp tp;
    private float prix;
    private boolean payer;
    private LocalDate date;
    private Rdv rdv;
    private Medecin medecin;

    public Paiement() {}

    public Paiement(float espece, Cheque cheque, float cb, Tp tp, float prix, boolean payer, LocalDate date) {
        this.espece = espece;
        this.cheque = cheque;
        this.cb = cb;
        this.tp = tp;
        this.prix = prix;
        this.payer = payer;
        this.date = date;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Basic
    public float getEspece() {
        return espece;
    }
    public void setEspece(float espece) {
        this.espece = espece;
    }

    @OneToOne
    @PrimaryKeyJoinColumn
    public Cheque getCheque() {
        return cheque;
    }
    public void setCheque(Cheque cheque) {
        this.cheque = cheque;
    }

    @Basic
    public float getCb() {
        return cb;
    }
    public void setCb(float cb) {
        this.cb = cb;
    }

    @OneToOne
    @PrimaryKeyJoinColumn
    public Tp getTp() {
        return tp;
    }
    public void setTp(Tp tp) {
        this.tp = tp;
    }

    @Basic
    public float getPrix() {
        return prix;
    }
    public void setPrix(float prix) {
        this.prix = prix;
    }

    @Basic
    public boolean isPayer() {
        return payer;
    }
    public void setPayer(boolean payer) {
        this.payer = payer;
    }

    @Basic
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    @OneToOne
    public Rdv getRdv() {
        return rdv;
    }
    public void setRdv(Rdv rdv) {
        this.rdv = rdv;
    }

    @OneToOne
    private Medecin getMedecin() {
        return medecin;
    }
    private void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }
}
