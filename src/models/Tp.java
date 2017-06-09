package models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Tp")
public class Tp {

    private long id;
    private float montant;
	private LocalDate payer;
	private Paiement paiement;

    public Tp() {}


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Basic
    public float getMontant() {
        return montant;
    }
    public void setMontant(float montant) {
        this.montant = montant;
    }

    @Basic
    public LocalDate getPayer() {
        return payer;
    }

	public void setPayer(LocalDate payer) {
		this.payer = payer;
    }

    @OneToOne
    public Paiement getPaiement() {
		return paiement;
	}

    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
	}
}
