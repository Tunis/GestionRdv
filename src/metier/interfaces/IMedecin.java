package metier.interfaces;

import models.Medecin;

public interface IMedecin {

	boolean create(String name, String prenom, String telephone, String email);

	boolean delete(Medecin medecin);
	boolean save(Medecin medecin);

}
