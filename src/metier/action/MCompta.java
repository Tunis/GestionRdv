package metier.action;

import metier.hibernate.data.DataCompta;
import metier.hibernate.data.exceptions.DbCreateException;
import metier.hibernate.data.exceptions.DbGetException;
import metier.hibernate.data.exceptions.DbSaveException;
import models.Medecin;
import models.PresentDay;
import models.Rdv;
import models.compta.Compta;
import models.compta.ComptaJournaliere;
import models.compta.ComptaMensuelle;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public class MCompta{
    // TODO: 25/05/2017 a faire

	private DataCompta db;

	public MCompta() {
		this.db = new DataCompta();
	}

	public void save(ComptaJournaliere entity) {}

    public ComptaJournaliere getComptaOfDay(Medecin medecin, LocalDate date) throws DbGetException {
        return db.getComptaOfDay(medecin, date);
	}

	public void createComptaOfDay(Medecin medecin, LocalDate date, float retrait) throws DbSaveException, DbCreateException {
        // recuperation des rdv du jour :
        Optional<PresentDay> day = medecin.getPlannings().stream().filter(e -> e.getPresent().equals(date)).findFirst();
        if (day.isPresent()) {
            Optional<PresentDay> lastDay = medecin.getPlannings().stream().filter(e -> e.getPresent().isBefore(date)).sorted(PresentDay::compareTo).findFirst();
            List<Rdv> paiements;
            if (lastDay.isPresent()) {
                paiements = db.getPaiementUntil(medecin, date, lastDay.get().getPresent());
            } else {
                paiements = db.getPaiementUntil(medecin, date, null);
            }

            ComptaJournaliere cj = new ComptaJournaliere(medecin, date, retrait);

            paiements.forEach(r -> {
                cj.addTypeRdv(r.getTypeRdv());

                if (r.getPaiement() != null) {
                    if (r.getPaiement().getCb() != 0) {
                        cj.addNbCB(1);
                        cj.addCB(r.getPaiement().getCb());
                    }
                    if (r.getPaiement().getCheque() != null) {
                        cj.addNbCheque(1);
                        cj.addCheque(r.getPaiement().getCheque().getMontant());
                    }
                    if (r.getPaiement().getEspece() != 0) {
                        cj.addNbEspece(1);
                        cj.addEspece(r.getPaiement().getEspece());
                    }
                    if (r.getPaiement().getTp() != null) {
                        cj.addNbTp(1);
                        cj.addTp(r.getPaiement().getTp().getMontant());
                    }
                    cj.addImpayer((r.getPaiement().getDate() != null) ? 0 :
                            r.getPaiement().getPrix() -
                                    r.getPaiement().getCb() -
                                    r.getPaiement().getEspece() -
                                    ((r.getPaiement().getTp() != null) ? r.getPaiement().getTp().getMontant() : 0) -
                                    ((r.getPaiement().getCheque() != null) ? r.getPaiement().getCheque().getMontant() : 0));

                    cj.getPaiementList().add(r.getPaiement().clone());
                }
            });
            //db.saveComptaJPaiement(cj.getPaiementList());
            db.save(cj);
        } else throw new DbCreateException("medecin non present ce jour.");
    }

	public ComptaMensuelle loadComptaOfMonth(Medecin medecin, LocalDate date){
		if(medecin != null) {
			List<ComptaJournaliere> content = db.loadComptaOfMonth(medecin, date.getMonth(), date.getYear());
            ComptaMensuelle cm = new ComptaMensuelle(medecin, YearMonth.of(date.getYear(), date.getMonth()));
            cm.setComptaJournaliere(content);

			float soldePrecedent = 0f;
			for (ComptaJournaliere cj : cm.getComptaJournaliere()) {
				cm.addCS(cj.getNbCS());
				cm.addC2(cj.getNbC2());
				cm.addEcho(cj.getNbEcho());
				cm.addDIU(cj.getNbDIU());

				cm.addImpayer(cj.getImpayer());
				cm.addCheque(cj.getCheque());
				cm.addCB(cj.getCB());
                cm.addEspece(cj.getEspece());
                cm.addTp(cj.getTp());
				cm.addRetrait(cj.getRetrait());

				cm.addNbCB(cj.getNbCB());
				cm.addNbCheque(cj.getNbCheque());
				cm.addNbEspece(cj.getNbEspece());
				cm.addNbTp(cj.getNbTp());

				cj.setSoldePrecedent(soldePrecedent);
                soldePrecedent = soldePrecedent + (cj.getEspece() - cj.getRetrait());
            }
            cm.setSolde();
            return cm;
		}else{
			return null;
		}
	}

	public void print(Compta compta){
		// TODO: 29/05/2017 a faire
	}

    public boolean doesNotExist(Medecin medecin, LocalDate dateCompta) {
        try {
            ComptaJournaliere comptaOfDay = getComptaOfDay(medecin, dateCompta);
            return false;
        } catch (DbGetException e) {
            return true;
        }
    }
    /*
	creer compta journaliere
	calculer compta journaliere

	// quand creation de compta journaliere on recreer la compta du mois.

	creer compta mensuelle
	calculer compta mensuelle
	 */
}
