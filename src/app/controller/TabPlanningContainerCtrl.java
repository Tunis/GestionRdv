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
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import metier.action.MMedecin;
import models.Medecin;
import models.Rdv;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TabPlanningContainerCtrl {
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
	private ComboBox<Medecin> listMedecin;
	@FXML
	private ToggleGroup planningToggleGroup;
	private MMedecin mMedecin;
	private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>(LocalDate.now());
	private ObjectProperty<Medecin> medecin = new SimpleObjectProperty<>();

	public LocalDate getDate() {
		return date.get();
	}
	public ObjectProperty<LocalDate> dateProperty() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date.set(date);
	}

	public void setMainApp(Main mainApp, MMedecin mMedecin) {
        this.mainApp = mainApp;
        this.mMedecin = mMedecin;
        
        //planning first view
        mainApp.getPlanningContainer().setCenter(planningMois);

	    listMedecin.itemsProperty().bind(mMedecin.listProperty());
	    //TextFields.bindAutoCompletion(listMedecin.getEditor(), listMedecin.getItems());
	    
	    //Disable ToggleButton when clikOn
		planningToggleGroup.selectToggle(btnPlanningMonth);

		dateProperty().addListener((observable, oldValue, newValue) -> {
			draw();
		});

		medecin.addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				draw();
			}
		});
    }

	private void draw() {
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

	@FXML
    private void initialize() {
		listMedecin.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
			medecin.set(listMedecin.getItems().get((Integer) newValue));
		});
		planningJour = new CalendarDay();
		planningSemaine = new CalendarWeek();
		planningMois = new CalendarMonth();

		planningJour.itemProperty().bind(medecin);
		planningSemaine.itemProperty().bind(medecin);
		planningMois.itemProperty().bind(medecin);

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

	private void clickListener(Label l) {
		if (l.getProperties().get("rdv") != null) {
			Rdv rdv = (Rdv) l.getProperties().get("rdv");
			mainApp.showEditRdvDialog(rdv, null);
			changeView();
		} else {
			LocalDateTime dateCliked = (LocalDateTime) l.getProperties().get("date");
			if (listMedecin.getSelectionModel().isEmpty()) {
				// TODO: 02/06/2017 alert
			} else {
				mainApp.showCreateRdvDialog(dateCliked, listMedecin.getItems().get(listMedecin.getSelectionModel().getSelectedIndex()));
				changeView();
			}
		}
	}

	/*
		function to switch between view and update change
	 */
	@FXML
	private void changeView(){
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
}
