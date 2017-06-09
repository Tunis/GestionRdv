package app.controller;

import app.Main;
import app.view.custom.CalendarDay;
import app.view.custom.CalendarMonth;
import app.view.custom.CalendarView;
import app.view.custom.CalendarWeek;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import metier.action.MMedecin;
import models.Medecin;
import models.Rdv;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class TabPlanningContainerCtrl implements Initializable {
    private Main mainApp;
	
	private CalendarDay planningJour;
	private CalendarMonth planningMois;
	private CalendarWeek planningSemaine;

	@FXML
	private ToggleButton btnPlanningDay;
	// need to switch view :
	@FXML
	private ToggleButton btnPlanningWeek;
	@FXML
	private ToggleButton btnPlanningMonth;

	
	@FXML
    private TextField listMedecin;
    @FXML
	private ToggleGroup planningToggleGroup;
	private MMedecin mMedecin;
	private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>(LocalDate.now());

	public LocalDate getDate() {
		return date.get();
	}
	public ObjectProperty<LocalDate> dateProperty() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date.set(date);
	}

    private AutoCompletionBinding<Medecin> mAC;

	public void setMainApp(Main mainApp, MMedecin mMedecin) {
        this.mainApp = mainApp;
        this.mMedecin = mMedecin;

        planningJour.itemProperty().bind(mainApp.medecinProperty());
        planningSemaine.itemProperty().bind(mainApp.medecinProperty());
        planningMois.itemProperty().bind(mainApp.medecinProperty());
        
        //planning first view
        mainApp.getPlanningContainer().setCenter(planningMois);

	    //TextFields.bindAutoCompletion(listMedecin.getEditor(), listMedecin.getItems());
	    
	    //Disable ToggleButton when clikOn
		planningToggleGroup.selectToggle(btnPlanningMonth);

		dateProperty().addListener((observable, oldValue, newValue) -> {
			draw();
		});

        mainApp.medecinProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                listMedecin.setText(mainApp.getMedecin().toString());
                draw();
                System.out.println("draw medecin changed");
            }
		});
        mAC = TextFields.bindAutoCompletion(listMedecin, mMedecin.getList());
        mAC.setOnAutoCompleted(e -> {
            listMedecin.setText(e.getCompletion().toString());
            mainApp.medecinProperty().set(e.getCompletion());
        });
        mMedecin.listProperty().addListener((observable, oldValue, newValue) -> {
            mAC.dispose();
            mAC = TextFields.bindAutoCompletion(listMedecin, mMedecin.getList());
            mAC.setOnAutoCompleted(e -> {
                listMedecin.setText(e.getCompletion().toString());
                mainApp.medecinProperty().set(e.getCompletion());
            });
        });
    }

    public void draw() {
        ToggleButton actionBtn = (ToggleButton) planningToggleGroup.getSelectedToggle();
		if (actionBtn != null) {
			switch (actionBtn.getId()) {
				case "btnPlanningMonth":
					planningMois.draw();
					break;
				case "btnPlanningDay":
					planningJour.draw();
					break;
				case "btnPlanningWeek":
					planningSemaine.draw();
					break;
				default:
					break;
			}
		}
	}

    private void clickListener(Label l) {
        if (l.getProperties().get("rdv") != null) {
            Rdv rdv = (Rdv) l.getProperties().get("rdv");
            mainApp.showEditRdvDialog(rdv, null);
            changeView();
        } else {
            LocalDateTime dateCliked = (LocalDateTime) l.getProperties().get("date");
            if (mainApp.medecinProperty().isNull().get()) {
                // TODO: 02/06/2017 alert
            } else {
                mainApp.showCreateRdvDialog(dateCliked, mainApp.medecinProperty().get());
                changeView();
            }
        }
    }

    /*
        function to switch between view and update change
     */
    @FXML
    private void changeView() {
        ToggleButton actionBtn = (ToggleButton) planningToggleGroup.getSelectedToggle();
        draw();

        if (actionBtn != null) {
            switch (actionBtn.getId()) {
                case "btnPlanningMonth":
                    mainApp.getPlanningContainer().setCenter(planningMois);
                    break;
                case "btnPlanningDay":
                    mainApp.getPlanningContainer().setCenter(planningJour);
                    break;
                case "btnPlanningWeek":
                    mainApp.getPlanningContainer().setCenter(planningSemaine);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        planningJour = new CalendarDay();
		planningSemaine = new CalendarWeek();
		planningMois = new CalendarMonth();

		planningJour.dateProperty().bindBidirectional(dateProperty());
		planningSemaine.dateProperty().bindBidirectional(dateProperty());
		planningMois.dateProperty().bindBidirectional(dateProperty());

		// TODO: 02/06/2017 mise a jour des planning apres un ajout/modification d'un rdv
		// utilisation d'un EventBus la plus simple ici. et pas que pour la.


		// listener for click on cell :
		planningJour.getView().addEventFilter(MouseEvent.MOUSE_CLICKED, e-> {
			EventTarget target = e.getTarget();
			Label l = CalendarView.getCell(target);
			if(l != null){
				if(l.getProperties().get("date") != null) {
					clickListener(l);
				}
			}
		});

		planningSemaine.getView().addEventFilter(MouseEvent.MOUSE_CLICKED, e-> {
			EventTarget target = e.getTarget();
			Label l = CalendarView.getCell(target);
			if(l != null){
				clickListener(l);
			}
		});

		planningMois.getView().addEventFilter(MouseEvent.MOUSE_CLICKED, e-> {
			EventTarget target = e.getTarget();
			Label l = CalendarView.getCell(target);
			if(l != null){
				if(l.getProperties().get("date") != null) {
					LocalDate dateCliked = (LocalDate) l.getProperties().get("date");
					date.set(dateCliked);
					planningToggleGroup.selectToggle(btnPlanningDay);
					changeView();
				}
			}
		});


		planningToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if (newValue != null) {
					ToggleButton btnToggle;
					if (oldValue != null) {
						btnToggle = (ToggleButton) oldValue;
						btnToggle.disableProperty().set(false);
					}

					btnToggle = (ToggleButton) newValue;
					btnToggle.disableProperty().set(true);
					changeView();
				} else newValue = oldValue;
			}
		});


		btnPlanningMonth.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (btnPlanningMonth.equals(planningToggleGroup.getSelectedToggle())) {
					mouseEvent.consume();
				}
			}
		});
		btnPlanningWeek.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (btnPlanningWeek.equals(planningToggleGroup.getSelectedToggle())) {
					mouseEvent.consume();
				}
			}
		});
		btnPlanningDay.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (btnPlanningDay.equals(planningToggleGroup.getSelectedToggle())) {
					mouseEvent.consume();
				}
			}
		});
	}
}
