package calendar;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

/**
 * this class is the base for all the calendar view, each view extend this one
 * to used the same schema.
 * need a LocalDate and an Item to populate the view.
 */

public abstract class CalendarView<T> extends BorderPane {

	private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
	protected LocalDate getDate() {
		return date.get();
	}
	public ObjectProperty<LocalDate> dateProperty() {
		return date;
	}
	protected void setDate(LocalDate date) {
		this.date.set(date);
	}

	private ObjectProperty<T> item = new SimpleObjectProperty<>();
	protected T getItem() {
		return item.get();
	}
	public ObjectProperty<T> itemProperty() {
		return item;
	}
	protected void setItem(T item) {
		this.item.set(item);
	}

	protected Button prevBtn;
	protected Button suivBtn;
	protected Button todayBtn;
	protected Label dateLbl;
	private HBox navBar;
	private VBox top;


	protected CalendarView(){
		date.set(LocalDate.now());

		setNavBar();

		setView();

		setAction();

		setListener();
	}

	// define listener need
	protected void setListener(){
		date.addListener((observable, oldValue, newValue) -> {
			// changement de date.
			dateLbl.setText(getDateFormatted());
			draw();
		});

		item.addListener((observable, oldValue, newValue) -> {
			// changement d'item. (medecin)
			draw();
		});
	}

	protected void setNavBar() {
		prevBtn = new Button("<");
		suivBtn = new Button(">");
		todayBtn = new Button("Aujourd'hui");
		dateLbl = new Label(getDateFormatted());
		dateLbl.setMinSize(200, 0);
		dateLbl.setAlignment(Pos.CENTER);

		navBar = new HBox(prevBtn, dateLbl, suivBtn);
		navBar.setAlignment(Pos.CENTER);

		top = new VBox(navBar, todayBtn);
		top.setAlignment(Pos.CENTER);
		this.setTop(top);
	}

	/*
		---------------------------
		----- ABSTRACT METHOD -----
	    ---------------------------
	 */

	// define action for node.
	protected abstract void setAction();

	// make the drawing of the view
	protected abstract void draw();

	// formatted the date
	protected abstract String getDateFormatted();

	// define the view.
	protected abstract void setView();

}
