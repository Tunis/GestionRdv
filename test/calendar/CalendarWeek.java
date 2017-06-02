package calendar;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import models.Medecin;
import models.PresentDay;
import models.Rdv;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CalendarWeek extends CalendarView<Medecin> {


    private LocalDate dateStart;
    private LocalDate dateEnd;

    private GridPane view;
    public GridPane getView(){return view;}

    private ScrollPane scroll;

    public CalendarWeek(){
        super();
        draw();
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
        this.setCenter(scroll);
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
            System.out.println("date a afficher : " + j.format(DateTimeFormatter.ofPattern("EE dd", locale.get())));
            Label dayHeader = new Label(j.format(DateTimeFormatter.ofPattern("EE dd", locale.get())));
            view.add(dayHeader, j.getDayOfWeek().getValue(), 0);


            Map<LocalDate, PresentDay> presentDay = getPresentDay();
            int i = 1;
            Rdv rdv = null;

            for(int h = 9; h <= 20; h++) {

                for (int m = 0; m < 46; m += 15) {

                    if ((h == 19 && m == 0) || h < 19) {

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
                            Label cell = new Label(rdv != null ? rdv.toString() : "");
                            cell.setMinHeight(50);

                            cell.getStyleClass().add("calendar-cell");
                            cell.setMaxWidth(Integer.MAX_VALUE);
                            cell.setMaxHeight(Integer.MAX_VALUE);
                            GridPane.setHgrow(cell, Priority.ALWAYS);
                            GridPane.setVgrow(cell, Priority.ALWAYS);

                            if (rdv != null) {
                                GridPane.setRowSpan(cell, (int) (rdv.getDuration().toMinutes() / 15));
                                cell.getProperties().put("rdv", rdv);
                                cell.getStyleClass().add(rdv.getTypeRdv().name());
                            }

                            cell.getProperties().put("date", dateTime);

                            GridPane.setVgrow(cell, Priority.ALWAYS);
                            view.add(cell, j.getDayOfWeek().getValue(), i);

                            if (rdv != null && rdv.getDuration().toMinutes() / 15 == 1)
                                rdv = null;

                        } else {
                            if (!rdv.getTime().plus(rdv.getDuration()).isBefore(time))
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
        for(int h = 9; h <= 20; h++){
            for(int m = 0; m < 46; m += 15){
                if((h == 19 && m == 0) || h < 19) {
                    LocalTime time = LocalTime.of(h, m);
                    Label labelHeader = new Label(time.format(DateTimeFormatter.ofPattern("H:mm")));
                    labelHeader.setMinSize(50,50);
                    labelHeader.setMaxSize(50,50);
                    labelHeader.getStyleClass().add("calendar-day-header");
                    view.add(labelHeader, 0, i);
                }
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
