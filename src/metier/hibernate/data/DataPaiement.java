package metier.hibernate.data;

import metier.hibernate.Data;
import metier.hibernate.data.interfaces.IDbPaiement;
import models.Medecin;
import models.Paiement;

import java.time.LocalDate;
import java.util.List;

public class DataPaiement extends Data<Paiement> implements IDbPaiement{

	public DataPaiement(){super();}

	// a refaire si date == payer?
	// pour la page paiement.
	@Override
	public List<Paiement> getAllPaiementImpayer() {
		return session.createQuery("from Paiement where " +
				"date = null", Paiement.class)
				.getResultList();
	}

	// pour la compta
	@Override
	public List<Paiement> getAllPaiementOfDay(Medecin medecin, LocalDate date) {
		return session.createQuery("from Paiement p " +
				"where p.date = :date and " +
				"p.medecin = :medecin", Paiement.class)
				.setParameter("date", date)
				.setParameter("medecin", medecin)
				.getResultList();
	}
}
