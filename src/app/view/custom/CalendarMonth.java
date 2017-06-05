package app.view.custom;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
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

public class CalendarMonth extends CalendarView<Medecin> {

    private GridPane view;
    public GridPane getView(){return view;}

    public CalendarMonth(){
        super();
        configView();
        this.setCenter(view);
    }

    @Override
    protected void setActions() {
        // button action to change date :
        nextBtn.setOnAction(e -> {
            dateProperty().set(getDate().plusMonths(1));
        });
        prevBtn.setOnAction(e -> {
            dateProperty().set(getDate().minusMonths(1));
        });
        todayBtn.setOnAction(e -> dateProperty().set(LocalDate.now()));
    }

    @Override
    protected void configView() {
        view = new GridPane();
	    BorderPane.setMargin(view, new Insets(10, 10, 5, 10));
	    for (int i = 0; i < 7; i++) {
		    ColumnConstraints column = new ColumnConstraints();
		    column.setPercentWidth((double) 100 / 7);
		    RowConstraints row = new RowConstraints();
		    row.setPercentHeight((double) 100 / 6);
		    view.getColumnConstraints().add(column);
		    view.getRowConstraints().add(row);
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
        return date.get().format(DateTimeFormatter.ofPattern("MMMM u", locale.get()));
    }

    @Override
    protected void setContent() {
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


        // get le list of day for the item selected
        List<PresentDay> presentDay = getPresentDay(dateStart, dateEnd);

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

            label.getProperties().put("date",dateFind);
            label.setMaxSize(Integer.MAX_VALUE,Integer.MAX_VALUE);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setAlignment(Pos.CENTER);


            GridPane.setVgrow(label, Priority.ALWAYS);
            GridPane.setHgrow(label, Priority.ALWAYS);

            int dayOfWeek = d.get(weekFields.dayOfWeek());
            int daysSinceFirstDisplayed = (int) dateStart.until(d, ChronoUnit.DAYS);
            int weeksSinceFirstDisplayed = daysSinceFirstDisplayed / 7 ;


            view.add(label, dayOfWeek-1, weeksSinceFirstDisplayed + 1);
        }
    }

    @Override
    protected void setHeader() {
        for (int dayOfWeek = 1 ; dayOfWeek <= 7 ; dayOfWeek++) {
            DayOfWeek day = DayOfWeek.of(dayOfWeek);
            Label label = new Label(day.getDisplayName(TextStyle.FULL_STANDALONE, locale.get()));
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

    @Override
    protected void clearView() {
        view.getChildren().clear();
    }
}
