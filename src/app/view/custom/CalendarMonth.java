package app.view.custom;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;
import models.Medecin;
import models.PresentDay;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class CalendarMonth extends BorderPane {

    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    public LocalDate getDate() {
        return date.get();
    }
    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    private ObjectProperty<Medecin> item = new SimpleObjectProperty<>();
    public Medecin getItem() {
        return item.get();
    }
    public ObjectProperty<Medecin> itemProperty() {
        return item;
    }
    public void setItem(Medecin item) {
        this.item.set(item);
    }

    private GridPane view;
    public GridPane getView(){return view;}

    private Label showDate;
    private Button nextMonth;
    private Button prevMonth;
    private HBox top;

    public CalendarMonth(){
        // define actual date on creation :
        LocalDate date = LocalDate.now();
        this.date.set(date);

        // create Top Elements :
        configNavBar();

        // create view :
        configView();

        // listener on date change to redraw the view
        this.date.addListener((observable, oldValue, newValue) -> {
            if(oldValue != null && newValue != null){
                if(!oldValue.getMonth().equals(newValue.getMonth())){
                    redraw();
                }
            }
        });

        // listener on item change to redraw the view
        this.item.addListener((observable, oldValue, newValue) -> redraw());

        // button action to change date :
        nextMonth.setOnAction(e -> {
            dateProperty().set(getDate().plusMonths(1));
            showDate.setText(getDateFormatted());
        });
        prevMonth.setOnAction(e -> {
            dateProperty().set(getDate().minusMonths(1));
            showDate.setText(getDateFormatted());
        });

        // pupolating the calendar view the elements :
        this.setTop(top);
        this.setCenter(view);

        // drawing the view :
        redraw();
    }

    private void configView(){
        view = new GridPane();
        view.setCenterShape(true);
        view.setHgap(0);
        view.setVgap(0);

        view.getStylesheets().add(getClass().getResource("calendar.css").toExternalForm());
        view.getStyleClass().add("calendar-grid");

        GridPane.setFillHeight(view,true);
        GridPane.setFillWidth(view, true);
        GridPane.setValignment(view, VPos.CENTER);
        GridPane.setHalignment(view, HPos.CENTER);
        GridPane.setVgrow(view, Priority.ALWAYS);
        GridPane.setHgrow(view, Priority.ALWAYS);
    }

    private void configNavBar(){
        nextMonth = new Button(">");
        prevMonth = new Button("<");
        showDate = new Label(this.getDateFormatted());
        nextMonth.setAlignment(Pos.CENTER_RIGHT);
        prevMonth.setAlignment(Pos.CENTER_LEFT);
        showDate.setAlignment(Pos.CENTER);
        showDate.setMinSize(100,0);
        top = new HBox(prevMonth, showDate, nextMonth);
        top.setSpacing(20);
        top.setAlignment(Pos.CENTER);
        top.setFillHeight(true);
    }

    private void redraw() {
        // remove all child
        view.getChildren().clear();

        // define locale and date need to render the view
        Locale locale = Locale.getDefault();
        WeekFields weekFields = WeekFields.of(locale);
        YearMonth month = YearMonth.of(date.get().getYear(), date.get().getMonth());
        LocalDate firstDay = month.atDay(1);
        LocalDate lastDay = month.atEndOfMonth();
        LocalDate dateStart;
        if(firstDay.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            dateStart = firstDay;
        }else{
            dateStart = firstDay.minusDays(firstDay.getDayOfWeek().getValue()-1);
        }
        LocalDate dateEnd = lastDay.plusDays(7-(lastDay.getDayOfWeek().getValue()));

        // draw the header of the view :
        setHeader(locale);

        // get le list of day for the item selected
        List<PresentDay> presentDay = getPresentDay(dateStart, dateEnd);

        // draw the day grid
        setContent(weekFields, dateStart, dateEnd, presentDay);
    }

    private void setContent(WeekFields weekFields, LocalDate dateStart, LocalDate dateEnd, List<PresentDay> presentDay) {
        for (LocalDate d = dateStart ; ! d.isAfter(dateEnd) ; d = d.plusDays(1)) {
            final LocalDate dateFind = d;
            Optional<PresentDay> first = presentDay.stream().filter(p -> p.getPresent().equals(dateFind)).findFirst();
            Label label;

            if(first.isPresent()) {
                label = new Label(String.valueOf(d.getDayOfMonth()) + "\n" + first.get().getRdvList().size() + " rdv.");
            }else{
                label = new Label(String.valueOf(d.getDayOfMonth()));
            }
            label.getStylesheets().add(getClass().getResource("calendar.css").toExternalForm());
            if(d.getMonth().equals(date.get().getMonth())){
                label.getStyleClass().add("calendar-cell");
            }else{
                label.getStyleClass().add("calendar-cell-not-display-month");
            }

            label.setUserData(d);
            label.setMaxSize(Integer.MAX_VALUE,Integer.MAX_VALUE);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setAlignment(Pos.CENTER);
            label.setCenterShape(true);


            GridPane.setVgrow(label, Priority.ALWAYS);
            GridPane.setHgrow(label, Priority.ALWAYS);

            int dayOfWeek = d.get(weekFields.dayOfWeek());
            int daysSinceFirstDisplayed = (int) dateStart.until(d, ChronoUnit.DAYS);
            int weeksSinceFirstDisplayed = daysSinceFirstDisplayed / 7 ;


            view.add(label, dayOfWeek-1, weeksSinceFirstDisplayed + 1);
        }
    }

    private List<PresentDay> getPresentDay(LocalDate dateStart, LocalDate dateEnd){
        if(item.get() != null)
            return item.get().getPlannings().stream()
                    .filter(p->
                            p.getPresent().isAfter(dateStart.minusDays(1)) &&
                                    p.getPresent().isBefore(dateEnd.plusDays(1))
                    )
                    .collect(Collectors.toList());
        else return new ArrayList<>();
    }

    public String getDateFormatted() {
        return date.get().format(DateTimeFormatter.ofPattern("MMMM u"));
    }

    private void setHeader(Locale locale){
        for (int dayOfWeek = 1 ; dayOfWeek <= 7 ; dayOfWeek++) {
            DayOfWeek day = DayOfWeek.of(dayOfWeek);
            Label label = new Label(day.getDisplayName(TextStyle.FULL_STANDALONE, locale));
            label.getStyleClass().add("calendar-day-header");
            label.setTextAlignment(TextAlignment.CENTER);
            label.setAlignment(Pos.CENTER);
            label.setCenterShape(true);
            GridPane.setFillHeight(label,false);
            GridPane.setFillWidth(label, true);
            GridPane.setValignment(label, VPos.CENTER);
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setVgrow(label, Priority.ALWAYS);
            GridPane.setHgrow(label, Priority.ALWAYS);
            view.add(label, dayOfWeek-1, 0);
        }
    }
}
