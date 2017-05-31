package calendar;

import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CalendarViewDay<T> extends CalendarView<T> {
	private GridPane view;

	private GridPane getView() {
		return view;
	}

	@Override
	protected void setAction() {
		prevBtn.setOnAction(e -> dateProperty().set(getDate().minusDays(1)));
		suivBtn.setOnAction(e -> dateProperty().set(getDate().plusDays(1)));
		todayBtn.setOnAction(e -> dateProperty().set(LocalDate.now()));
	}

	@Override
	protected void draw() {
		// TODO: 31/05/2017 generated the view here
	}

	@Override
	protected String getDateFormatted() {
		// format style : lundi 2 mai 2017
		return dateProperty().get().format(DateTimeFormatter.ofPattern("EEEE d MMMM yyyy"));
	}

	@Override
	protected void setView() {
		view = new GridPane();

		// TODO: 31/05/2017 config the view here
	}
}
