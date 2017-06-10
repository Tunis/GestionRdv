package metier.print;

import app.controller.printable.ComptaJCtrl;
import app.controller.printable.ComptaMCtrl;
import app.controller.printable.PlanningOfDayCtrl;
import javafx.fxml.FXMLLoader;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import models.PresentDay;
import models.compta.ComptaJournaliere;
import models.compta.ComptaMensuelle;

import java.io.IOException;

public class Printable {


	public static BorderPane createComptaM(ComptaMensuelle cm) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Printable.class.getResource("/printable/ComptaMensuelle.fxml"));
		BorderPane printable = loader.load();
		ComptaMCtrl ctrl = loader.getController();
		ctrl.initData(cm);

		return printable;
	}

	public static BorderPane createPlanning(PresentDay planning) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Printable.class.getResource("/printable/PlanningOfDay.fxml"));
		BorderPane printable = loader.load();
		PlanningOfDayCtrl ctrl = loader.getController();
		ctrl.initData(planning);

		return printable;
	}

	public static BorderPane createComptaJ(ComptaJournaliere cj) throws IOException {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Printable.class.getResource("/printable/ComptaJournaliere.fxml"));
		BorderPane printable = loader.load();
		ComptaJCtrl ctrl = loader.getController();
		ctrl.initData(cj);

		return printable;
	}

	public static void print(PrinterJob job, BorderPane view, PageOrientation orientation) throws PrintException {
		Stage tempStage = new Stage();
		tempStage.setScene(new Scene(view));
		tempStage.show();

		PageLayout pageLayout = job.getPrinter().createPageLayout(Paper.A4, orientation, 10, 10, 10, 10);
		job.getJobSettings().setPageLayout(pageLayout);
		double scaleY = 1;
		if (view.getHeight() > pageLayout.getPrintableHeight()) {
			scaleY = pageLayout.getPrintableHeight() / view.getHeight();
		}
		double scaleX = 1;
		if (view.getWidth() > pageLayout.getPrintableWidth()) {
			scaleX = pageLayout.getPrintableWidth() / view.getWidth();
		}
		view.getTransforms().add(new Scale(scaleX, scaleY));
		tempStage.hide();
		boolean b = job.printPage(view);
		if (b) {
			job.endJob();
		} else {
			throw new PrintException("Impossible d'effectuer l'impression.");
		}

	}
}
