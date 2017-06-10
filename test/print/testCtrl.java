package print;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import metier.action.MCompta;
import metier.action.MMedecin;
import metier.hibernate.data.DataCompta;
import metier.hibernate.data.exceptions.DbGetException;
import metier.print.Printable;
import models.Medecin;
import models.PresentDay;
import models.compta.ComptaJournaliere;
import models.compta.ComptaMensuelle;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class testCtrl implements Initializable {

    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

	public void print(BorderPane printable, PageOrientation type) {
		// TODO: 10/06/2017 change type to enum
		PrinterJob job = PrinterJob.createPrinterJob();
		job.showPrintDialog(stage);

		Stage stage = new Stage();
		stage.setScene(new Scene(printable));
		stage.show();

		if (type.equals(PageOrientation.LANDSCAPE)) {
			printLandscape(job, printable);
		} else {
			printPortrait(job, printable);
		}
		stage.hide();

		try {
			boolean b = job.printPage(printable);
			if (b) {
				job.endJob();
			} else {
				System.out.println("erreur?");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void printLandscape(PrinterJob job, BorderPane printable) {
		System.out.println("paysage");
		PageLayout pageLayout = job.getPrinter().createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, 10, 10, 10, 10);
		job.getJobSettings().setPageLayout(pageLayout);
		double scaleY = 1;
		if (printable.getHeight() > pageLayout.getPrintableHeight()) {
			scaleY = pageLayout.getPrintableHeight() / printable.getHeight();
		}
		double scaleX = 1;
		if (printable.getWidth() > pageLayout.getPrintableWidth()) {
			scaleX = pageLayout.getPrintableWidth() / printable.getWidth();
		}
		printable.getTransforms().add(new Scale(scaleX, scaleY));

	}

	public void printPortrait(PrinterJob job, BorderPane printable) {
		System.out.println("portrait");
		PageLayout pageLayout = job.getPrinter().createPageLayout(Paper.A4, PageOrientation.PORTRAIT, 10, 10, 10, 10);
		job.getJobSettings().setPageLayout(pageLayout);
		double scaleY = 1;
		if (printable.getHeight() > pageLayout.getPrintableHeight()) {
			scaleY = pageLayout.getPrintableHeight() / printable.getHeight();
		}
		double scaleX = 1;
		if (printable.getWidth() > pageLayout.getPrintableWidth()) {
			scaleX = pageLayout.getPrintableWidth() / printable.getWidth();
		}
		printable.getTransforms().add(new Scale(scaleX, scaleY));
	}


    public void setStage(Stage stage) {
        this.stage = stage;
    }

	@FXML
	private void printComptaJ(ActionEvent actionEvent) {
		MMedecin mMedecin = new MMedecin();
		Medecin medecin = mMedecin.getList().get(0);
		LocalDate date = LocalDate.of(2017, 6, 9);
		DataCompta db = new DataCompta();
		ComptaJournaliere cj = null;
		try {
			cj = db.getComptaOfDay(medecin, date);
		} catch (DbGetException e) {
			e.printStackTrace();
		}
		if (cj != null) {
			try {
				print(Printable.createComptaJ(cj), PageOrientation.LANDSCAPE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	private void printPlanning(ActionEvent actionEvent) {
		MMedecin mMedecin = new MMedecin();
		Medecin medecin = mMedecin.getList().get(0);
		LocalDate date = LocalDate.of(2017, 6, 9);
		Optional<PresentDay> plaaning = medecin.getPlannings().stream().filter(p -> p.getPresent().equals(date)).findFirst();
		if (plaaning.isPresent()) {
			try {
				print(Printable.createPlanning(plaaning.get()), PageOrientation.PORTRAIT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@FXML
	private void printComptaM(ActionEvent actionEvent) {
		MMedecin mMedecin = new MMedecin();
		Medecin medecin = mMedecin.getList().get(0);
		LocalDate date = LocalDate.of(2017, 6, 9);
		MCompta mCompta = new MCompta();
		ComptaMensuelle cm = mCompta.loadComptaOfMonth(medecin, date);
		try {
			print(Printable.createComptaM(cm), PageOrientation.PORTRAIT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void printRdvList(ActionEvent actionEvent) {

        /*
            get data here for now
         */
		MMedecin mMedecin = new MMedecin();
		Medecin medecin = mMedecin.getList().get(0);
		LocalDate date = LocalDate.of(2017, 6, 9);

	}
}
