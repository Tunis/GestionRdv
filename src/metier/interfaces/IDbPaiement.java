package metier.interfaces;

import models.Medecin;
import models.Paiement;

import java.time.LocalDate;
import java.util.List;

public interface IDbPaiement extends IDb<Paiement> {

	List<Paiement> getAllPaiementImpayer();
	List<Paiement> getAllPaiementOfDay(Medecin medecin, LocalDate date);
}
