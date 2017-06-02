package app.view.custom;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import models.Medecin;
import models.PresentDay;
import models.Rdv;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class CalendarDay extends CalendarView<Medecin> {

    private GridPane view;
    public GridPane getView(){return view;}

    private ScrollPane scroll;

    public CalendarDay(){
        super();
        draw();
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
        this.setCenter(scroll);
    }

    @Override
    public String getDateFormatted() {
        return date.get().format(DateTimeFormatter.ofPattern("EEEE dd MMMM u", locale.get()));
    }

    @Override
    protected void setContent() {
        Optional<PresentDay> planning = getPresentDay();
        int i = 1;
        Rdv rdv = null;

        for(int h = 9; h <= 20; h++){

            for(int m = 0; m < 46; m += 15){

                if((h == 19 && m == 0) || h < 19) {

                    LocalTime time = LocalTime.of(h, m);
                    LocalDateTime dateTime = LocalDateTime.of(date.get(), time);
                    if (rdv == null) {
                        if (planning.isPresent()) {
                            Optional<Rdv> rdvTime = planning.get().getRdvList().stream().filter(r -> r.getTime().equals(time)).findFirst();
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
                        view.add(cell, 1, i);

                        if(rdv != null && rdv.getDuration().toMinutes()/15 == 1)
                            rdv = null;

                    }else {
                        if(!rdv.getTime().plus(rdv.getDuration()).isBefore(time))
                            rdv = null;
                    }
                }
                i++;
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

    private Optional<PresentDay> getPresentDay(){
        if(item.get() != null)
            return item.get().getPlannings().stream()
                    .filter(p-> p.getPresent().equals(date.get()))
                    .findFirst();
        else return Optional.empty();
    }
}
