package calendar;

import com.sun.javafx.scene.control.skin.LabeledText;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Medecin;

import java.time.LocalDate;


public class Test extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
//		testMonth(primaryStage);
		testWeek(primaryStage);
//		testDay(primaryStage);
	}

	private void testMonth(Stage stage){
		CalendarViewMonth<Medecin> cal = new CalendarViewMonth();


		// click listener pour aller a la bonne date :
		cal.getView().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			Object target = event.getTarget();
			Label l = null;
			if(target instanceof Label){
				l = (Label) target;
			}else if(target instanceof LabeledText){
				LabeledText lt = (LabeledText) target;
				l = (Label) lt.getParent();
			}
			if(l != null){
				// TODO: 31/05/2017 on recupere la date selectionner ici surement avoir un field date pour pouvoir ecouter ses changement
				LocalDate dateSelected = (LocalDate) l.getUserData();
			}
		});
		stage.setScene(new Scene(cal));
		stage.show();
	}
	private void testWeek(Stage stage){
		CalendarViewWeek cal = new CalendarViewWeek();

		stage.setScene(new Scene(cal));
		stage.show();
	}
	private void testDay(Stage stage){
		CalendarViewDay cal = new CalendarViewDay();

		stage.setScene(new Scene(cal));
		stage.show();
	}
}
