package test;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import metier.hibernate.data.DataMedecin;
import models.Medecin;
import models.PresentDay;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CalendarTest extends BorderPane{

    private DataMedecin db = new DataMedecin();

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

    private GridPane view;

    public CalendarTest(){
        this.getStylesheets().add(getClass().getResource("calendar.css").toExternalForm());
        view = new GridPane();
        view.setCenterShape(true);

        view.getStylesheets().add(getClass().getResource("calendar.css").toExternalForm());
        this.getStyleClass().add("calendar");
        view.getStyleClass().add("calendar-grid");

        GridPane.setFillHeight(view,true);
        GridPane.setFillWidth(view, true);
        GridPane.setValignment(view, VPos.CENTER);
        GridPane.setHalignment(view, HPos.CENTER);
        GridPane.setVgrow(view, Priority.ALWAYS);
        GridPane.setHgrow(view, Priority.ALWAYS);
        GridPane.setMargin(view, new Insets(10));
        LocalDate date = LocalDate.of(2017,5,6);

        this.date.set(date);
        this.date.addListener((observable, oldValue, newValue) -> {
            if(oldValue != null && newValue != null){
                if(!oldValue.getMonth().equals(newValue.getMonth())){
                    redraw();
                }
            }
        });
        redraw();
    }

    private void redraw() {
        view.getChildren().clear();
        Locale locale = Locale.getDefault();
        WeekFields weekFields = WeekFields.of(locale);

        YearMonth month = YearMonth.of(date.get().getYear(), date.get().getMonth());
        LocalDate firstDay = month.atDay(1);
        System.out.println("first day of month : " + firstDay);
        LocalDate lastDay = month.atEndOfMonth();
        System.out.println("last day of month : " + lastDay);
        LocalDate dateStart;
        if(firstDay.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            dateStart = firstDay;
        }else{
            dateStart = firstDay.minusDays(firstDay.getDayOfWeek().getValue()-1);
        }
        System.out.println("first day to show : " + dateStart);
        LocalDate dateEnd = lastDay.plusDays(7-(lastDay.getDayOfWeek().getValue()));
        System.out.println("last day to show : " + dateEnd);

        int days = dateStart.until(dateEnd).getDays();

        Medecin medecin = db.getAll().get(0);
        List<PresentDay> presentDays = medecin.getPlannings().stream()
                .filter(p->
                        p.getPresent().isAfter(dateStart) &&
                                p.getPresent().isBefore(dateEnd)
                )
                .collect(Collectors.toList());


        PseudoClass beforeMonth = PseudoClass.getPseudoClass("before-display-month");
        PseudoClass afterMonth = PseudoClass.getPseudoClass("after-display-month");

        // header definitions

        for (int dayOfWeek = 1 ; dayOfWeek <= 7 ; dayOfWeek++) {
            DayOfWeek day = DayOfWeek.of(dayOfWeek);
            Label label = new Label(day.getDisplayName(TextStyle.FULL_STANDALONE, locale));
            label.getStyleClass().add("calendar-day-header");
            label.setMaxSize(Integer.MAX_VALUE,Integer.MAX_VALUE);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setAlignment(Pos.CENTER);
            label.setCenterShape(true);
            GridPane.setFillHeight(label,true);
            GridPane.setFillWidth(label, true);
            GridPane.setValignment(label, VPos.CENTER);
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setVgrow(label, Priority.ALWAYS);
            GridPane.setHgrow(label, Priority.ALWAYS);
            view.add(label, dayOfWeek-1, 0);
        }

        int nbRow = 0;
        for (LocalDate d = dateStart ; ! d.isAfter(dateEnd) ; d = d.plusDays(1)) {
            Label label = new Label(String.valueOf(d.getDayOfMonth()));
            label.getStylesheets().add(getClass().getResource("calendar.css").toExternalForm());
            if(d.getMonth().equals(date.get().getMonth())){
                label.getStyleClass().add("calendar-cell");
            }else{
                label.getStyleClass().add("calendar-cell-not-display-month");
            }
//            label.pseudoClassStateChanged(beforeMonth, d.isBefore(firstDay));
//            label.pseudoClassStateChanged(afterMonth, d.isAfter(lastDay));
            label.setUserData(d);
            label.setMaxSize(Integer.MAX_VALUE,Integer.MAX_VALUE);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setAlignment(Pos.CENTER);
            label.setCenterShape(true);
            label.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THIN)));

            label.setOnMouseClicked(event -> {
                Label l = (Label)event.getSource();
                System.out.println("il est le : " + l.getUserData()); // localDate
            });

            GridPane.setFillHeight(label,true);
            GridPane.setFillWidth(label, true);
            GridPane.setValignment(label, VPos.CENTER);
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setVgrow(label, Priority.ALWAYS);
            GridPane.setHgrow(label, Priority.ALWAYS);


            int dayOfWeek = d.get(weekFields.dayOfWeek());
            int daysSinceFirstDisplayed = (int) dateStart.until(d, ChronoUnit.DAYS);
            int weeksSinceFirstDisplayed = daysSinceFirstDisplayed / 7 ;


            view.add(label, dayOfWeek-1, weeksSinceFirstDisplayed + 1);
        }
    }

    public GridPane getView() {
        return view;
    }

    public void setView(GridPane view) {
        this.view = view;
    }
}
