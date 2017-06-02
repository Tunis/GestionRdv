package calendar;

import com.sun.javafx.scene.control.skin.LabeledText;
import javafx.application.Application;
import javafx.event.EventTarget;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import metier.hibernate.DataBase;
import metier.hibernate.data.DataMedecin;
import models.Medecin;
import models.Rdv;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class Test extends Application {
    private DataMedecin db;

    @Override
    public void start(Stage primaryStage) throws Exception {

        db = new DataMedecin();

        primaryStage.setOnCloseRequest(e->DataBase.close());

        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(600);
        //primaryStage.setMaximized(true);


//        testMonth(primaryStage);
        testWeek(primaryStage);
//        testDay(primaryStage);
    }

    private void testMonth(Stage stage) {
        CalendarMonth cal = new CalendarMonth();

        // listener for click :
        cal.getView().addEventFilter(MouseEvent.MOUSE_CLICKED, e-> {
            EventTarget target = e.getTarget();
            Label l = null;
            if(target instanceof LabeledText){
                LabeledText lt = (LabeledText) target;
                l = (Label) lt.getParent();
            }else if (target instanceof Label){
                l = (Label) target;
            }
            if(l != null){
                if(l.getProperties().get("date") != null) {
                    LocalDate dateCliked = (LocalDate) l.getUserData();
                    System.out.println("click on " + dateCliked);
                }
            }
        });

        stage.setScene(new Scene(cal));
        stage.show();
    }

    private void testWeek(Stage stage) {
        CalendarWeek cal = new CalendarWeek();
        Optional<Medecin> m = db.getAll().stream().filter(med -> med.getId() == 21).findFirst();
        if(m.isPresent()){
            cal.setItem(m.get());
        }
        // listener for click :
        cal.getView().addEventFilter(MouseEvent.MOUSE_CLICKED, e-> {
            EventTarget target = e.getTarget();
            Label l = null;
            if(target instanceof LabeledText){
                LabeledText lt = (LabeledText) target;
                l = (Label) lt.getParent();
            }else if (target instanceof Label){
                l = (Label) target;
            }
            if(l != null){
                LocalDate dateCliked = (LocalDate) l.getUserData();
                System.out.println("click on " + dateCliked);
            }
        });

        stage.setScene(new Scene(cal));
        stage.show();
    }

    private void testDay(Stage stage) {
        CalendarDay cal = new CalendarDay();

        Optional<Medecin> m = db.getAll().stream().filter(med -> med.getId() == 21).findFirst();
        if(m.isPresent()){
            cal.setItem(m.get());
        }


        // listener for click :
        cal.getView().addEventFilter(MouseEvent.MOUSE_CLICKED, e-> {
            EventTarget target = e.getTarget();
            Label l = null;
            if(target instanceof LabeledText){
                LabeledText lt = (LabeledText) target;
                l = (Label) lt.getParent();
            }else if (target instanceof Label){
                l = (Label) target;
            }
            if(l != null){
                if(l.getProperties().get("date") != null) {
                    if(l.getProperties().get("rdv") != null){
                        Rdv rdv = (Rdv) l.getProperties().get("rdv");
                    }else {
                        LocalDateTime dateCliked = (LocalDateTime) l.getProperties().get("date");
                    }
                }
            }
        });

        stage.setScene(new Scene(cal));
        stage.show();
    }


}
