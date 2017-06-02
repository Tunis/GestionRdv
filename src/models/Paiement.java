package models;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "Paiement")
public class Paiement implements Serializable, Comparable<Paiement>{
	private static final long serialVersionUID = 1L;
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

    public Paiement(float espece, Cheque cheque, float cb, Tp tp, float prix, boolean payer, LocalDate date, Medecin medecin, Rdv rdv) {
        this.espece = espece;
        this.cheque = cheque;
        this.cb = cb;
        this.tp = tp;
        this.prix = prix;
        this.payer = payer;
        this.date = date;
        this.rdv = rdv;
        this.medecin = medecin;
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

    @OneToOne(cascade = CascadeType.MERGE)
    public Rdv getRdv() {
        return rdv;
    }
    public void setRdv(Rdv rdv) {
        this.rdv = rdv;
    }

    @OneToOne(cascade = CascadeType.MERGE)
    private Medecin getMedecin() {
        return medecin;
    }
	private void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Paiement paiement = (Paiement) o;

        if (Float.compare(paiement.getEspece(), getEspece()) != 0) return false;
        if (Float.compare(paiement.getCb(), getCb()) != 0) return false;
        if (Float.compare(paiement.getPrix(), getPrix()) != 0) return false;
        if (isPayer() != paiement.isPayer()) return false;
        if (getCheque() != null ? !getCheque().equals(paiement.getCheque()) : paiement.getCheque() != null)
            return false;
        if (getTp() != null ? !getTp().equals(paiement.getTp()) : paiement.getTp() != null) return false;
        if (!getDate().equals(paiement.getDate())) return false;
        if (!getRdv().equals(paiement.getRdv())) return false;
        return getMedecin().equals(paiement.getMedecin());
    }

    @Override
    public int hashCode() {
        int result = (getEspece() != +0.0f ? Float.floatToIntBits(getEspece()) : 0);
        result = 31 * result + (getCheque() != null ? getCheque().hashCode() : 0);
        result = 31 * result + (getCb() != +0.0f ? Float.floatToIntBits(getCb()) : 0);
        result = 31 * result + (getTp() != null ? getTp().hashCode() : 0);
        result = 31 * result + (getPrix() != +0.0f ? Float.floatToIntBits(getPrix()) : 0);
        result = 31 * result + (isPayer() ? 1 : 0);
        result = 31 * result + getDate().hashCode();
        result = 31 * result + getRdv().hashCode();
        result = 31 * result + getMedecin().hashCode();
        return result;
    }


    @Override
    public int compareTo(Paiement o) {
        return rdv.getPresentDay().getPresent().compareTo(o.getRdv().getPresentDay().getPresent());
    }
}
