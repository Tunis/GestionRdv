package app.controller;

import app.Main;
import app.view.custom.CalendarDay;
import app.view.custom.CalendarMonth;
import app.view.custom.CalendarView;
import app.view.custom.CalendarWeek;
import com.sun.javafx.scene.control.skin.LabeledText;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import metier.action.MMedecin;
import models.Medecin;
import models.Rdv;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TabPlanningContainerCtrl {
	private Main mainApp;
	
	private CalendarDay planningJour;
	private CalendarMonth planningMois;
	private CalendarWeek planningSemaine;
	
	@FXML
	private Button btnPlanningPrevious;
	@FXML
	private Button btnPlanningToday;
	@FXML
	private Button btnPlanningNext;
	@FXML
	private ToggleButton btnPlanningDay;
	@FXML
	private ToggleButton btnPlanningWeek;
	@FXML
	private ToggleButton btnPlanningMonth;
	
	@FXML
	private Label labelTypePlanning;
	
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
	    planningToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
	    	@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
	    		ToggleButton btnToggle = (ToggleButton)oldValue;
	    		btnToggle.disableProperty().set(false);
	    		
	    		btnToggle = (ToggleButton)newValue;
	    		btnToggle.disableProperty().set(true);
	    		changeView();
			}
		});
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




		// listener for click on cell :
		planningJour.getView().addEventFilter(MouseEvent.MOUSE_CLICKED, e-> {
			EventTarget target = e.getTarget();
			Label l = CalendarView.getCell(target);
			if(l != null){
				if(l.getProperties().get("date") != null) {
					if(l.getProperties().get("rdv") != null){
						Rdv rdv = (Rdv) l.getProperties().get("rdv");
						mainApp.showEditRdvDialog(rdv);
					}else {
						LocalDateTime dateCliked = (LocalDateTime) l.getProperties().get("date");
						mainApp.showCreateRdvDialog();
					}
				}
			}
		});

		planningSemaine.getView().addEventFilter(MouseEvent.MOUSE_CLICKED, e-> {
			EventTarget target = e.getTarget();
			Label l = CalendarView.getCell(target);
			if(l != null){
				if(l.getProperties().get("rdv") != null){
					Rdv rdv = (Rdv) l.getProperties().get("rdv");
					mainApp.showEditRdvDialog(rdv);
				}else {
					LocalDateTime dateCliked = (LocalDateTime) l.getProperties().get("date");
					mainApp.showCreateRdvDialog();
				}
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
	}
	
	//Load view et set controller
	private BorderPane setController(String urlView){
		try {
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(Main.class.getResource(urlView));
	        loader.setController(this);
	        BorderPane planning = (BorderPane)loader.load();
	        
	        return planning;
		} catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	@FXML
	private void changeView(){
		ToggleButton actionBtn = (ToggleButton) planningToggleGroup.getSelectedToggle();
		
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

	@SuppressWarnings("unused")
	private void changeData(){
		if (mainApp.getPlanningContainer().getCenter().equals(planningMois)) {

		}else if(mainApp.getPlanningContainer().getCenter().equals(planningSemaine)){

		}else{

		}
	}
}
