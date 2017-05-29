package models;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Medecin {

    private long id;
    private String firstName;
    private String lastName;
    private String telephone;
    private String email;
    private List<PresentDay> plannings = new ArrayList<>();


    public Medecin() {}

    public Medecin(String firstName, String lastName, String telephone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephone = telephone;
        this.email = email;
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
    @Column(length = 50)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(length = 50)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(length = 16)
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Basic
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.EXTRA)
    public List<PresentDay> getPlannings() {
        return plannings;
    }

    public void setPlannings(List<PresentDay> plannings) {
        this.plannings = plannings;
    }
}
