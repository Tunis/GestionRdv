package app.controller;

import app.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;

public class TabPlanningContainerCtrl {
	private Main mainApp;
	
	private BorderPane planningJour;
	private BorderPane planningMois;
	private BorderPane planningSemaine;
	
	private ObservableList<String> comboBox = FXCollections.observableArrayList();
	
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
	private ComboBox<String> listMedecin;
	@FXML
	private ToggleGroup planningToggleGroup;
    
	public TabPlanningContainerCtrl(){
		//Load all view
        planningJour = setController("view/PlanningJourOverview.fxml");
        planningMois = setController("view/PlanningMoisOverview.fxml");
        planningSemaine = setController("view/PlanningSemaineOverview.fxml");
	}
	
	public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        
        //planning first view
        mainApp.getPlanningContainer().setCenter(planningMois);
        
        comboBox.addAll("Fred", "Fred2", "Totoro", "Nausicaa");
	    listMedecin.setItems(comboBox);
	    TextFields.bindAutoCompletion(listMedecin.getEditor(), listMedecin.getItems());
	    
	    //Disable ToggleButton when clikOn
	    planningToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
	    	@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
	    		ToggleButton btnToggle = (ToggleButton)oldValue;
	    		btnToggle.disableProperty().set(false);
	    		
	    		btnToggle = (ToggleButton)newValue;
	    		btnToggle.disableProperty().set(true);
			}
		});
    }
	
	@FXML
    private void initialize() {}
	
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
	private void changeView(ActionEvent event){
		ToggleButton actionBtn = (ToggleButton)event.getSource();
		
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
