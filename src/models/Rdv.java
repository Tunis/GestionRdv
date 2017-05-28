package models;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalTime;

@Entity
@Table(name = "Rdv")
public class Rdv {

    private long id;
    private String cotation;

    private Duration duration;
    private TypeRdv typeRdv;
    private LocalTime time;
    private Patient patient;
    private PresentDay presentDay;
    private Paiement paiement;

    public Rdv() {}

    public Rdv(String cotation, Duration duration, TypeRdv typeRdv, LocalTime time, Patient patient, PresentDay presentDay) {
        this.cotation = cotation;
        this.duration = duration;
        this.typeRdv = typeRdv;
        this.time = time;
        this.patient = patient;
        this.presentDay = presentDay;
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
    @Column(nullable = false)
    public String getCotation() {
        return cotation;
    }
    public void setCotation(String cotation) {
        this.cotation = cotation;
    }

    @Basic
    @Column(nullable = false)
    public Duration getDuration() {
        return duration;
    }
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Enumerated
    public TypeRdv getTypeRdv() {
        return typeRdv;
    }
    public void setTypeRdv(TypeRdv typeRdv) {
        this.typeRdv = typeRdv;
    }

    @Basic
    public LocalTime getTime() {
        return time;
    }
    public void setTime(LocalTime time) {
        this.time = time;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public PresentDay getPresentDay() {
        return presentDay;
    }
    public void setPresentDay(PresentDay presentDay) {
        this.presentDay = presentDay;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public Paiement getPaiement() {
        return paiement;
    }
    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }
}
