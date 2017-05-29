package models;

import javax.persistence.*;

@Entity
@Table(name = "Tp")
public class Tp {

    private long id;
    private float montant;
    private boolean payer;

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
    public boolean isPayer() {
        return payer;
    }
    public void setPayer(boolean payer) {
        this.payer = payer;
    }
}
