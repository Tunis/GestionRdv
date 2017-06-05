package metier.action;

import metier.hibernate.data.DataCompta;
import metier.hibernate.data.exceptions.DbCreateException;
import metier.hibernate.data.exceptions.DbSaveException;
import models.Medecin;
import models.PresentDay;
import models.Rdv;
import models.compta.Compta;
import models.compta.ComptaJournaliere;
import models.compta.ComptaMensuelle;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MCompta{
// TODO: 25/05/2017 a faire

	private DataCompta db;

	public MCompta() {
		this.db = new DataCompta();
	}

	public void save(ComptaJournaliere entity) {}

	public ComptaJournaliere getComptaOfDay(Medecin medecin, LocalDate date) {
		return db.getComptaOfDay(medecin, date);
	}

	public void createComptaOfDay(Medecin medecin, LocalDate date, float retrait) throws DbSaveException, DbCreateException {
		// TODO: 03/06/2017 on recup la liste des rdv du jour et la liste des paiement effectué aujourd'hui on evite les paiement correspondant au rdv
		Optional<PresentDay> first = medecin.getPlannings().stream().filter(e -> e.getPresent().equals(date)).findFirst();
		List<Rdv> rdvs = new ArrayList<>();
		if(first.isPresent()) {
			// liste des rdv du jour
			rdvs = first.get().getRdvList();
            for (Rdv rdv : rdvs) {
                System.out.println("rdv de : " + rdv.getPatient());
                System.out.println("rdv le : " + rdv.getPresentDay().getPresent());

            }

			// TODO: 03/06/2017 recup liste des paiement du jour et filtrage de ceux correspondant a un rdv du jour.
			// TODO: 05/06/2017 verifier les date des paiement pour mettre en impayer si date rdv != date paiement

            ComptaJournaliere cj = new ComptaJournaliere(medecin, date, retrait);

            rdvs.forEach(r -> {
                cj.addTypeRdv(r.getTypeRdv());

                if(r.getPaiement() != null) {
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
	                cj.getPaiementList().add(r.getPaiement());
                }
            });
            System.out.println(cj);
            db.save(cj);
        }else throw new DbCreateException("medecin non present ce jour.");
	}

	public ComptaMensuelle loadComptaOfMonth(Medecin medecin, LocalDate date){
		if(medecin != null) {
			List<ComptaJournaliere> content = db.loadComptaOfMonth(medecin, date.getMonth(), date.getYear());
			ComptaMensuelle cm = new ComptaMensuelle(medecin, date.getMonth(), date.getYear());
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
				cm.addEspece(cj.getEspece() - cj.getRetrait());
				cm.addTp(cj.getTp());
				cm.addRetrait(cj.getRetrait());

				cm.addNbCB(cj.getNbCB());
				cm.addNbCheque(cj.getNbCheque());
				cm.addNbEspece(cj.getNbEspece());
				cm.addNbTp(cj.getNbTp());

				cj.setSoldePrecedent(soldePrecedent);
				soldePrecedent = cj.getEspece() - cj.getRetrait();
			}
			return cm;
		}else{
			return null;
		}
	}

	public void print(Compta compta){
		// TODO: 29/05/2017 a faire
	}
	/*
	creer compta journaliere
	calculer compta journaliere

	// quand creation de compta journaliere on recreer la compta du mois.

	creer compta mensuelle
	calculer compta mensuelle
	 */
}
