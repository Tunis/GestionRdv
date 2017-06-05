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
    private LocalDate date;
    private Rdv rdv;
    private Medecin medecin;

    public Paiement() {}

	public Paiement(float espece, Cheque cheque, float cb, Tp tp, float prix, LocalDate date, Rdv rdv) {
		this.espece = espece;
        this.cheque = cheque;
        this.cb = cb;
        this.tp = tp;
        this.prix = prix;
        this.date = date;
        this.rdv = rdv;
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

	@OneToOne(cascade = CascadeType.ALL)
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

	@OneToOne(cascade = CascadeType.ALL)
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
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

	@OneToOne(cascade = CascadeType.ALL)
	public Rdv getRdv() {
        return rdv;
    }
    public void setRdv(Rdv rdv) {
        this.rdv = rdv;
    }

	@OneToOne(cascade = CascadeType.ALL)
	public Medecin getMedecin() {
		return medecin;
    }

	public void setMedecin(Medecin medecin) {
		this.medecin = medecin;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
	    if (!(o instanceof Paiement)) return false;

        Paiement paiement = (Paiement) o;

	    if (id != paiement.id) return false;
	    if (Float.compare(paiement.espece, espece) != 0) return false;
	    if (Float.compare(paiement.cb, cb) != 0) return false;
	    if (Float.compare(paiement.prix, prix) != 0) return false;
	    if (cheque != null ? !cheque.equals(paiement.cheque) : paiement.cheque != null) return false;
	    if (tp != null ? !tp.equals(paiement.tp) : paiement.tp != null) return false;
	    if (date != null ? !date.equals(paiement.date) : paiement.date != null) return false;
	    if (!medecin.equals(paiement.rdv)) return false;
	    return rdv.equals(paiement.rdv);
    }

    @Override
    public int hashCode() {
        int result = (getEspece() != +0.0f ? Float.floatToIntBits(getEspece()) : 0);
        result = 31 * result + (getCheque() != null ? getCheque().hashCode() : 0);
        result = 31 * result + (getCb() != +0.0f ? Float.floatToIntBits(getCb()) : 0);
        result = 31 * result + (getTp() != null ? getTp().hashCode() : 0);
        result = 31 * result + (getPrix() != +0.0f ? Float.floatToIntBits(getPrix()) : 0);
        result = 31 * result + (isPayer() ? 1 : 0);
	    result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
	    result = 31 * result + getRdv().hashCode();
        result = 31 * result + getMedecin().hashCode();
        return result;
    }


    @Override
    public int compareTo(Paiement o) {
        return rdv.getPresentDay().getPresent().compareTo(o.getRdv().getPresentDay().getPresent());
    }

	@Transient
	public boolean isPayer() {
		return (espece + cb + ((cheque != null) ? cheque.getMontant() : 0) +
				((tp != null) ? tp.getMontant() : 0)) == prix;
	}
}
