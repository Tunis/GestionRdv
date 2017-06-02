package app.view.custom;

import com.sun.javafx.scene.control.skin.LabeledText;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventTarget;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.Locale;

public abstract class CalendarView<T> extends BorderPane {
    protected ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    public LocalDate getDate() {
        return date.get();
    }
    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    protected ObjectProperty<T> item = new SimpleObjectProperty<>();
    public T getItem() {
        return item.get();
    }
    public ObjectProperty<T> itemProperty() {
        return item;
    }
    public void setItem(T item) {
        this.item.set(item);
    }

    protected ObjectProperty<Locale> locale = new SimpleObjectProperty<>();
    public Locale getLocale() {
        return locale.get();
    }
    public ObjectProperty<Locale> localeProperty() {
        return locale;
    }
    public void setLocale(Locale locale) {
        this.locale.set(locale);
    }

    //    private GridPane view;
//    public GridPane getView(){return view;}

    protected Label dateLbl;
    protected Button todayBtn;
    protected Button nextBtn;
    protected Button prevBtn;
    protected HBox navBar;
    protected VBox top;

    public CalendarView(){
        // define actual date on creation :
        LocalDate date = LocalDate.now();
        this.date.set(date);

        locale.set(Locale.getDefault());

        // define css stylesheet :
        this.getStylesheets().add(getClass().getResource("calendar.css").toExternalForm());

        // create Top Elements :
        configNavBar();

        // create view :
        configView();

        setListeners();

        setActions();

        // button action to change date :
        // TODO: 01/06/2017 action setter abstract

        // pupolating the calendar view the elements :
    }

    private void configNavBar(){
        nextBtn = new Button(">");
        prevBtn = new Button("<");
        dateLbl = new Label(this.getDateFormatted());

        nextBtn.setAlignment(Pos.CENTER_RIGHT);
        prevBtn.setAlignment(Pos.CENTER_LEFT);
        dateLbl.setAlignment(Pos.CENTER);
        dateLbl.setMinSize(400,0);
        dateLbl.setAlignment(Pos.CENTER_LEFT);

        navBar = new HBox(prevBtn, nextBtn, dateLbl);
        navBar.setSpacing(20);
        navBar.setAlignment(Pos.CENTER);
        navBar.setFillHeight(true);

        todayBtn = new Button("Today");

        top = new VBox(navBar, todayBtn);
        top.setAlignment(Pos.CENTER);

        this.setTop(top);
        BorderPane.setAlignment(top, Pos.CENTER);
    }

    protected void draw() {
        // TODO: 01/06/2017 juste use the abstract setter method
        clearView();

        setHeader();

        setContent();
    }

    protected void setListeners(){
        // listener on date change to redraw the view
        date.addListener((observable, oldValue, newValue) -> {
            if(oldValue != null && newValue != null){
                if(!oldValue.equals(newValue)){
                    dateLbl.setText(getDateFormatted());
                    draw();
                }
            }
        });

        // listener on item change to redraw the view
        item.addListener((observable, oldValue, newValue) -> draw());

        // listener on locale :
        locale.addListener((observable, oldValue, newValue) -> draw());
    }

    protected abstract void setActions();
    protected abstract void configView();
    public abstract String getDateFormatted();
    protected abstract void setContent();
    protected abstract void setHeader();
    protected abstract void clearView();


    public static Label getCell(EventTarget target){
        if(target instanceof LabeledText){
            LabeledText lt = (LabeledText) target;
            return (Label) lt.getParent();
        }else if (target instanceof Label){
            return (Label) target;
        }
        return null;
    }
}
