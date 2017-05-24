package app.models;

public class Medecin {
	private String nom;
	private String prenom;
	private String mail;
	private String tel;
	
	//Construct
	//----------------------------------------
	public Medecin(){
		this(null, null);
	}
	
	public Medecin(String nom, String prenom){
		this.nom = nom;
		this.prenom = prenom;
	}

	//Getter
	//----------------------------------------
	public String getNom() {
		return nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public String getMail() {
		return mail;
	}

	public String getTel() {
		return tel;
	}

	//Setter
	//----------------------------------------
	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
}
