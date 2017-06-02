package models;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Callback;
import models.enums.TypeRdv;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalTime;

@Entity
@Table(name = "Rdv")
public class Rdv {

    private long id;
    private String cotation;

    private ObjectProperty<Duration> duration = new SimpleObjectProperty<>();
    private TypeRdv typeRdv;
    private LocalTime time;
    private Patient patient;
    private PresentDay presentDay;
    private Paiement paiement;

    public Rdv() {}

    public Rdv(String cotation, Duration duration, TypeRdv typeRdv, LocalTime time, Patient patient, PresentDay presentDay) {
        this.cotation = cotation;
        this.duration.set(duration);
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
        return duration.get();
    }
    public void setDuration(Duration duration) {
        this.duration.set(duration);
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

    @ManyToOne(cascade = CascadeType.MERGE)
    @PrimaryKeyJoinColumn
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @ManyToOne(cascade = CascadeType.MERGE)
    @PrimaryKeyJoinColumn
    public PresentDay getPresentDay() {
        return presentDay;
    }
    public void setPresentDay(PresentDay presentDay) {
        this.presentDay = presentDay;
    }

    @OneToOne(cascade = CascadeType.REFRESH)
    public Paiement getPaiement() {
        return paiement;
    }
    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }

//    @Override
//    public String toString() {
//        return patient.getFirstName() + " " + patient.getMaidenName();
//    }
    public static Callback<Rdv, Observable[]> extractor() {
        return (Rdv p) -> new Observable[]{p.duration};
    }
}
