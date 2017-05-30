package models;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Patient")
public class Patient implements Serializable, Comparable<Patient>{
	private static final long serialVersionUID = 1L;
	private long id;
    private String lastName;
    private String firstName;
    private String maidenName;
    private String telephone;
    private String email;
    private String note;
    private int secuNumber;

    private LocalDate bornDate;
    private Adresse adresse;
    private List<Rdv> rdvList = new ArrayList<>();

    public Patient() {}

    public Patient(String lastName, String firstName, String maidenName, String telephone, String email, String note, int secuNumber, LocalDate bornDate, Adresse adresse) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.maidenName = maidenName;
        this.telephone = telephone;
        this.email = email;
        this.note = note;
        this.secuNumber = secuNumber;
        this.bornDate = bornDate;
        this.adresse = adresse;
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
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(nullable = false)
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(nullable = false)
    public String getMaidenName() {
        return maidenName;
    }
    public void setMaidenName(String maidenName) {
        this.maidenName = maidenName;
    }

    @Basic
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

    @Basic
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    @Basic
    public int getSecuNumber() {
        return secuNumber;
    }
    public void setSecuNumber(int secuNumber) {
        this.secuNumber = secuNumber;
    }

    @Basic
    @Column(nullable = false)
    public LocalDate getBornDate() {
        return bornDate;
    }
    public void setBornDate(LocalDate bornDate) {
        this.bornDate = bornDate;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public Adresse getAdresse(){return adresse;}
    public void setAdresse(Adresse adresse){ this.adresse = adresse;}

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.EXTRA)
    public List<Rdv> getRdvList() {
        return rdvList;
    }
    public void setRdvList(List<Rdv> rdvList) {
        this.rdvList = rdvList;
    }


    @Override
    public String toString() {
        return lastName + " " + firstName + " " + maidenName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Patient patient = (Patient) o;

        if (getLastName() != null ? !getLastName().equals(patient.getLastName()) : patient.getLastName() != null)
            return false;
        if (!getFirstName().equals(patient.getFirstName())) return false;
        if (!getMaidenName().equals(patient.getMaidenName())) return false;
        return getBornDate().equals(patient.getBornDate());
    }

    @Override
    public int hashCode() {
        int result = getLastName() != null ? getLastName().hashCode() : 0;
        result = 31 * result + getFirstName().hashCode();
        result = 31 * result + getMaidenName().hashCode();
        result = 31 * result + getBornDate().hashCode();
        return result;
    }

    @Override
    public int compareTo(Patient o) {
        if(maidenName.compareTo(o.getMaidenName()) == 0){
            if(firstName.compareTo(o.getFirstName()) == 0){
                if((o.getLastName() != null && lastName != null) && lastName.compareTo(o.getLastName()) == 0){
                    return o.getBornDate().compareTo(bornDate);
                }else {
                    if(o.getLastName() != null && lastName != null) return lastName.compareTo(o.getLastName());
                    if(o.getLastName() == null && lastName == null) return 0;
                    if(o.getLastName() == null) return 1;
                    if(lastName == null) return -1;
                    return 0;
                }
            }else return firstName.compareTo(o.getFirstName());
        }else return maidenName.compareTo(o.getMaidenName());
    }
}
