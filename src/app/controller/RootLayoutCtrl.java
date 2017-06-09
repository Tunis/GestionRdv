package app.controller;

import app.Main;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

import java.net.URL;
import java.util.ResourceBundle;

public class RootLayoutCtrl implements Initializable {
	
	private Main mainApp;
	
	//Tab
    //-----------------------------------
	@FXML
    private Tab TabHome;
	@FXML
    private Tab TabPlanning;
    @FXML
    private Tab TabPaiement;
    @FXML
    private Tab TabTP;
    @FXML
    private Tab TabCompta;
	
    //Main m�thods
    //-----------------------------------
	public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

	//Set content for all Tab
	public void setView(){
		TabHome.setContent(mainApp.homeOverview());
		TabPaiement.setContent(mainApp.paiementOverview());
		TabCompta.setContent(mainApp.comptaOverview());
		TabPlanning.setContent(mainApp.planningContainer());
		TabTP.setContent(mainApp.tiersPayantOverview());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		TabPaiement.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
			if (newValue)
				mainApp.getTabPaiementOverviewCtrl().updateData();
		});
        TabTP.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue)
                mainApp.getTabTpCtrl().updateData();
        });
        TabPlanning.selectedProperty().addListener((observable, oldValue, newValue) -> {
            mainApp.getTabPlanningCtrl().draw();
        });
    }


}
