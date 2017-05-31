package calendar;

import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CalendarViewWeek<T> extends CalendarView<T> {

	private GridPane view;

	private GridPane getView() {
		return view;
	}

	@Override
	protected void setAction() {
		prevBtn.setOnAction(e -> dateProperty().set(getDate().minusWeeks(1)));
		suivBtn.setOnAction(e -> dateProperty().set(getDate().plusWeeks(1)));
		todayBtn.setOnAction(e -> dateProperty().set(LocalDate.now()));
	}

	@Override
	protected void draw() {
		// TODO: 31/05/2017 generated the view here
	}

	@Override
	protected String getDateFormatted() {
		// TODO: 31/05/2017 a voir avec christophe comment afficher la date ici
		return dateProperty().get().format(DateTimeFormatter.ofPattern("MMMM yyyy \n W"));
	}

	@Override
	protected void setView() {
		view = new GridPane();

		// TODO: 31/05/2017 config the view here
	}
}
