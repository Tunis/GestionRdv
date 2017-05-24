package metier.hibernate;

import models.Paiement;

import java.time.LocalDate;
import java.util.List;

public class DataPaiement extends Data<Paiement> {

	public DataPaiement(){super();}

	public List<Paiement> getPaiementOfDay(LocalDate date) {
		return session.createQuery("from Paiement where " +
				"date = :date", Paiement.class)
				.setParameter("date", date)
				.getResultList();
	}

	public List<Paiement> getMissingPaiement() {
		return session.createQuery("from Paiement where " +
				"payer = false", Paiement.class)
				.getResultList();
	}
}
