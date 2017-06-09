package app.view.custom;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;
import models.Medecin;
import models.PresentDay;
import models.Rdv;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CalendarWeek extends CalendarView<Medecin> {


    private LocalDate dateStart;
    private LocalDate dateEnd;

    private GridPane view;
    public GridPane getView(){return view;}

    private ScrollPane scroll;
    private int minH = 8;
    private int maxH = 20;

    public CalendarWeek(){
        super();
    }


    @Override
    protected void setActions() {
        // button action to change date :
        nextBtn.setOnAction(e -> {
            dateProperty().set(getDate().plusWeeks(1));
        });
        prevBtn.setOnAction(e -> {
            dateProperty().set(getDate().minusWeeks(1));
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
	    view.setAlignment(Pos.CENTER);
	    this.setCenter(scroll);
	    BorderPane.setMargin(scroll, new Insets(10, 10, 5, 10));
	    ColumnConstraints columnH = new ColumnConstraints();
	    view.getColumnConstraints().add(columnH);
	    for (int i = 0; i < 7; i++) {
		    ColumnConstraints column = new ColumnConstraints();
		    column.setPercentWidth((double) 100 / 8);
		    view.getColumnConstraints().add(column);
	    }
	    view.setOnScroll(new ScrollHandler());
    }

    @Override
    public String getDateFormatted() {
        getWeekDate();
        return "Du " +
            dateStart.format(DateTimeFormatter.ofPattern("dd MMMM u", locale.get())) +
            " au " +
            dateEnd.format(DateTimeFormatter.ofPattern("dd MMMM u", locale.get()));
    }

    @Override
    protected void setContent() {
        for(LocalDate j = dateStart; j.isBefore(dateEnd.plusDays(1)); j = j.plusDays(1)){
            Label dayHeader = new Label(j.format(DateTimeFormatter.ofPattern("EE dd", locale.get())));
            dayHeader.setAlignment(Pos.CENTER);
            dayHeader.setMaxSize(Integer.MAX_VALUE,Integer.MAX_VALUE);
            dayHeader.setTextAlignment(TextAlignment.CENTER);
            view.add(dayHeader, j.getDayOfWeek().getValue(), 0);


            Map<LocalDate, PresentDay> presentDay = getPresentDay();
            int i = 1;
            Rdv rdv = null;
	        long until = 0;

            for (int h = minH; h < maxH; h++) {

                for (int m = 0; m < 46; m += 15) {

                        LocalTime time = LocalTime.of(h, m);
                        LocalDate dateDay = j;
                        LocalDateTime dateTime = LocalDateTime.of(j, time);
                        if (rdv == null) {
                            if (!presentDay.isEmpty()) {
                                Optional<Rdv> rdvTime = presentDay.getOrDefault(dateDay, new PresentDay()).getRdvList().stream()
                                        .filter(r ->
                                                r.getTime().equals(time) &&
                                                r.getPresentDay().getPresent().equals(dateDay)).findFirst();
                                if (rdvTime.isPresent()) {
                                    rdv = rdvTime.get();
                                }
                            }
                            Label cell = new Label(rdv != null ? rdv.getPatient().showName() : "");
                            cell.setWrapText(false);
                            cell.setMinHeight(50);

                            cell.getStyleClass().add("calendar-cell");
                            cell.setMaxWidth(Integer.MAX_VALUE);
                            cell.setMaxHeight(Integer.MAX_VALUE);
                            GridPane.setHgrow(cell, Priority.ALWAYS);
                            GridPane.setVgrow(cell, Priority.ALWAYS);

                            if (rdv != null) {
	                            until = time.until(rdv.getTime().plusMinutes(rdv.getDuration().toMinutes()), ChronoUnit.MINUTES);
	                            GridPane.setRowSpan(cell, (int) (until / 15));
	                            cell.getProperties().put("rdv", rdv);
                                cell.getStyleClass().add(rdv.getTypeRdv().name());
                            }

                            cell.getProperties().put("date", dateTime);

                            GridPane.setVgrow(cell, Priority.ALWAYS);
                            view.add(cell, j.getDayOfWeek().getValue(), i);

	                        if (rdv != null && until / 15 == 1)
		                        rdv = null;

                        } else {
	                        if (time.until(rdv.getTime().plusMinutes(rdv.getDuration().toMinutes()), ChronoUnit.MINUTES) <= 15) {
		                        rdv = null;
	                        }
                        }
                    i++;
                }
            }
        }
    }

    @Override
    protected void setHeader() {
        int i = 1;
        for (int h = minH; h < maxH; h++) {
            for(int m = 0; m < 46; m += 15){
                    LocalTime time = LocalTime.of(h, m);
                    Label labelHeader = new Label(time.format(DateTimeFormatter.ofPattern("H:mm")));
	                GridPane.setValignment(labelHeader, VPos.TOP);
	                labelHeader.setAlignment(Pos.TOP_CENTER);
	                labelHeader.setMinSize(50,50);
                    labelHeader.setMaxSize(50,50);
                    labelHeader.getStyleClass().add("calendar-day-header");
                    view.add(labelHeader, 0, i);
                i++;
            }
        }
    }

    @Override
    protected void clearView() {
        view.getChildren().clear();
    }

    private void getWeekDate(){
        DayOfWeek dayOfWeek = date.get().getDayOfWeek();
        int toFirstDay = dayOfWeek.getValue();
        dateStart = date.get().minusDays(toFirstDay-1);
        dateEnd = dateStart.plusDays(6);
    }

    private Map<LocalDate, PresentDay> getPresentDay(){
        if(item.get() != null)
            return item.get().getPlannings().stream()
                    .filter(p-> p.getPresent().isBefore(dateEnd.plusDays(1)) &&
                    p.getPresent().isAfter(dateStart.minusDays(1))).collect(Collectors.toMap(PresentDay::getPresent, Function.identity()));
        else return new HashMap<>();
    }
}
