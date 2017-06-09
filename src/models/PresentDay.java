package models;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PresentDay")
public class PresentDay implements Serializable, Comparable<PresentDay> {

    private long id;
    private LocalDate date;
    private Medecin medecin;

    private List<Rdv> rdvList = new ArrayList<>();

    public PresentDay() {
    }

    public PresentDay(LocalDate present, Medecin medecin) {
        this.date = present;
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

    @Column(nullable = false)
    public LocalDate getPresent() {
        return date;
    }
    public void setPresent(LocalDate present) {
        this.date = present;
    }

    @ManyToOne
    @PrimaryKeyJoinColumn
    public Medecin getMedecin(){return medecin;}
    public void setMedecin(Medecin medecin){ this.medecin = medecin;}

    @OneToMany(mappedBy = "presentDay", orphanRemoval = true, cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    public List<Rdv> getRdvList() {
        return rdvList;
    }
    public void setRdvList(List<Rdv> rdvList) {
        this.rdvList = rdvList;
    }

    @Override
    public int compareTo(PresentDay o) {
        return o.getPresent().compareTo(this.getPresent());
    }
}
