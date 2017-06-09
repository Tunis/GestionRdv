package app.view.custom;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import models.Medecin;
import models.PresentDay;
import models.Rdv;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class CalendarDay extends CalendarView<Medecin> {

    private GridPane view;
    public GridPane getView(){return view;}

    private ScrollPane scroll;

	private int minH = 8;
	private int maxH = 20;
	private int midItem = 0;
	private Optional<PresentDay> planning = null;
	private Rdv rdv = null;

    public CalendarDay(){
        super();
	    midItem = (maxH - minH) * 2;
    }

    @Override
    protected void setActions() {
        // button action to change date :
        nextBtn.setOnAction(e -> {
            dateProperty().set(getDate().plusDays(1));
        });
        prevBtn.setOnAction(e -> {
            dateProperty().set(getDate().minusDays(1));
        });
        todayBtn.setOnAction(e -> dateProperty().set(LocalDate.now()));
    }

    @Override
    protected void configView() {
        scroll = new ScrollPane();
        view = new GridPane();
        view.setCenterShape(true);
        view.setAlignment(Pos.CENTER_LEFT);
        view.setMinWidth(100);


        scroll.setContent(view);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

	    ColumnConstraints col = new ColumnConstraints();
	    col.setPercentWidth(45);
	    ColumnConstraints colH = new ColumnConstraints();
	    colH.setPercentWidth(5);
	    view.getColumnConstraints().add(colH);
	    view.getColumnConstraints().add(col);
	    view.getColumnConstraints().add(colH);
	    view.getColumnConstraints().add(col);
	    view.setOnScroll(new ScrollHandler());


        this.setCenter(scroll);
    }

    @Override
    public String getDateFormatted() {
        return date.get().format(DateTimeFormatter.ofPattern("EEEE dd MMMM u", locale.get()));
    }

    @Override
    protected void setContent() {
	    planning = getPresentDay();
	    int i = 1;
	    int e = 0;

	    for (int h = minH; h < maxH; h++) {
		    for (int m = 0; m < 46; m += 15) {
			    LocalTime time = LocalTime.of(h, m);

                    LocalDateTime dateTime = LocalDateTime.of(date.get(), time);
                    if (rdv == null) {
	                    cellFactory(i, e, time, dateTime);
                    } else {
	                    // verifier si rdv est en chevauchement sur la separation
	                    if (e == 1 && !rdv.getTime().plus(rdv.getDuration()).isBefore(time)) {
		                    cellFactory(i, e, time, dateTime);
	                    } else if (time.until(rdv.getTime().plusMinutes(rdv.getDuration().toMinutes()), ChronoUnit.MINUTES) <= 15) {
                            rdv = null;
	                    }
                    }
			    if (i < midItem) {
				    i++;
			    } else if (i + 1 == midItem) {
				    i++;
				    e++;
			    } else {
				    e++;
				    i++;
			    }

            }
        }
    }

    @Override
    protected void setHeader() {
        int i = 1;
	    int e = 1;
	    for (int h = minH; h < maxH; h++) {
		    for(int m = 0; m < 46; m += 15){
                    LocalTime time = LocalTime.of(h, m);
                    Label labelHeader = new Label(time.format(DateTimeFormatter.ofPattern("H:mm")));
                    labelHeader.setMinSize(50,50);
	            labelHeader.setMaxSize(Integer.MAX_VALUE, 50);
			    labelHeader.getStyleClass().add("calendar-day-header");
	            if (i <= midItem) {
		            view.add(labelHeader, 0, i);
		            i++;
	            } else {
		            view.add(labelHeader, 2, e);
		            e++;
	            }
            }
        }
    }

    @Override
    protected void clearView() {
        view.getChildren().clear();
    }

    private Optional<PresentDay> getPresentDay(){
        if(item.get() != null)
            return item.get().getPlannings().stream()
                    .filter(p-> p.getPresent().equals(date.get()))
                    .findFirst();
        else return Optional.empty();
    }


	private void cellFactory(int i, int e, LocalTime time, LocalDateTime dateTime) {
		if (planning.isPresent()) {
			Optional<Rdv> rdvTime = planning.get().getRdvList().stream().filter(r -> r.getTime().equals(time)).findFirst();
			if (rdvTime.isPresent()) {
				rdv = rdvTime.get();
			}
		}
        Label cell = new Label(rdv != null ? rdv.getPatient().showName() : "");
        cell.setMinHeight(50);

		cell.getStyleClass().add("calendar-cell");
		cell.setMaxWidth(Integer.MAX_VALUE);
		cell.setMaxHeight(Integer.MAX_VALUE);
		GridPane.setHgrow(cell, Priority.ALWAYS);
		GridPane.setVgrow(cell, Priority.ALWAYS);
		long until = 0;

		if (rdv != null) {
			until = time.until(rdv.getTime().plusMinutes(rdv.getDuration().toMinutes()), ChronoUnit.MINUTES);
			if (i <= midItem && (rdv.getDuration().toMinutes() / 15) - 1 + i <= midItem)
				GridPane.setRowSpan(cell, (int) (rdv.getDuration().toMinutes() / 15));
			else if (i <= midItem && (rdv.getDuration().toMinutes() / 15) - 1 + i > midItem) {
				GridPane.setRowSpan(cell, (midItem - i) + 1);
			} else if (e == 1) {
				GridPane.setRowSpan(cell, (int) until / 15);
			} else {
				GridPane.setRowSpan(cell, (int) rdv.getDuration().toMinutes() / 15);
			}
			cell.getProperties().put("rdv", rdv);
			cell.getStyleClass().add(rdv.getTypeRdv().name());
		}

		cell.getProperties().put("date", dateTime);

		GridPane.setVgrow(cell, Priority.ALWAYS);

		if (i <= midItem) {
			view.add(cell, 1, i);
		} else {
			view.add(cell, 3, e);
		}
		if (rdv != null && until / 15 == 1) {
			rdv = null;
		}
	}
}

