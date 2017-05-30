package models;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Medecin implements Serializable, Comparable<Medecin> {
	private static final long serialVersionUID = 1L;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Medecin medecin = (Medecin) o;

        if (!getFirstName().equals(medecin.getFirstName())) return false;
        if (!getLastName().equals(medecin.getLastName())) return false;
        if (!getTelephone().equals(medecin.getTelephone())) return false;
        return getEmail() != null ? getEmail().equals(medecin.getEmail()) : medecin.getEmail() == null;
    }

    @Override
    public int hashCode() {
        int result = getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + getTelephone().hashCode();
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Medecin o) {
        return (firstName.compareTo(o.getFirstName()) == 0) ?
                lastName.compareTo(o.getLastName()):
                firstName.compareTo(o.getFirstName());
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
