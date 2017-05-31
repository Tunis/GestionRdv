package calendar;

import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CalendarViewMonth<T> extends CalendarView<T> {

	private GridPane view;
	public GridPane getView() {
		return view;
	}

	public CalendarViewMonth(){super();}

	@Override
	protected void setAction() {
		prevBtn.setOnAction(e-> dateProperty().set(getDate().minusMonths(1)));
		suivBtn.setOnAction(e-> dateProperty().set(getDate().plusMonths(1)));
		todayBtn.setOnAction(e -> dateProperty().set(LocalDate.now()));
	}

	@Override
	protected void draw() {
		// TODO: 31/05/2017 generated the view here
	}

	@Override
	protected String getDateFormatted() {
		// format style : juin 2018
		return dateProperty().get().format(DateTimeFormatter.ofPattern("MMMM yyyy"));
	}

	@Override
	protected void setView() {
		view = new GridPane();

		// TODO: 31/05/2017 config the view here
	}
}
