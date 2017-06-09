package print;

import java.time.LocalDate;
import java.time.LocalTime;

public class TestItem {

    private String nom;
    private LocalDate date;
    private LocalTime time;

    public TestItem(String nom, LocalDate date, LocalTime time) {
        this.nom = nom;
        this.date = date;
        this.time = time;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
