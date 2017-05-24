package app.models;

import java.time.LocalDate;

public class Patient {
	private String nom;
	private String prenom;
	private String nomJeuneFille;
	private String adresse;
	private String tel;
	private int nSecu;
	private LocalDate dateNaissance;
	private String note;
	
	//Construct
	//----------------------------------------
	public Patient(){
		this(null, null, null, null);
	}
	
	public Patient(String nom, String prenom, String nomJF, LocalDate dateNaissance){
		this.nom = nom;
		this.prenom = prenom;
		this.nomJeuneFille = nomJF;
		this.dateNaissance = dateNaissance;
	}

	//Getter
	//----------------------------------------
	public String getNom() {
		return nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public String getNomJeuneFille() {
		return nomJeuneFille;
	}

	public String getAdresse() {
		return adresse;
	}

	public String getTel() {
		return tel;
	}

	public int getnSecu() {
		return nSecu;
	}

	public LocalDate getDateNaissance() {
		return dateNaissance;
	}

	public String getNote() {
		return note;
	}

	//Setter
	//----------------------------------------
	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public void setNomJeuneFille(String nomJeuneFille) {
		this.nomJeuneFille = nomJeuneFille;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public void setnSecu(int nSecu) {
		this.nSecu = nSecu;
	}

	public void setDateNaissance(LocalDate dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
